package mirrg.boron.util.hopper2.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mirrg.boron.util.UtilsFile;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import mirrg.boron.util.suppliterator.SuppliteratorCollectors;

/**
 * キューの内容が一定量を超えると、無制限にファイルに格納するホッパーです。
 * また、終了時にホッパー内にアイテムが残っていると、それをファイルシステムに格納して終了し、
 * 再起動時にそれらを読み込みます。
 * このホッパーにnullを入れることはできません。
 *
 * @deprecated このクラスは実験的です。メジャーバージョンの変更なしに破壊的変更が行われる可能性があります。
 */
@Deprecated // 実験的。 TODO 検討
public abstract class HopperSwapping<I> extends Hopper<I>
{

	protected final File dir;
	protected final int swapSize;

	public HopperSwapping(File dir, int swapSize)
	{
		this.dir = dir;
		this.swapSize = swapSize;
	}

	protected int currentPosition = 0;
	protected SwapFiles swapFiles = new SwapFiles();
	protected boolean isTerminated = false;

	/*
	 * アイテムをキューに入れる。
	 * 入れた後、スワップファイルがない場合は、現在位置の更新を行う。
	 * その後、「キューサイズ-現在位置」がスワップ化サイズと等しければ、
	 * 現在位置より先をすべて削除し、スワップファイルを作成し、スワップリストを更新する。
	 */
	/**
	 * @throws IllegalStateException
	 *             terminated状態の時に呼び出した場合
	 */
	@Override
	public void push(I item)
	{
		Objects.requireNonNull(item);
		synchronized (lock) {

			// キュー空き待ち
			while (true) {

				// このホッパーは終了処理がされた
				if (isTerminated) throw new IllegalStateException("Terminated hopper");

				// このホッパーは既に閉じられている
				if (isClosed()) throw new IllegalStateException("Closed hopper");

				// 空きが生まれた
				if (canPush()) break;

				throw new AssertionError();

			}

			// キューに追加
			queue.addLast(item);

			// 現在位置を更新する
			updateCurrentPosition();

			// ファイル化の必要がある場合、ファイル化する
			int tailCount = queue.size() - currentPosition;
			if (tailCount >= swapSize) {

				// キューの末尾から現在位置まで全部取り出し、ライターに食べさせる
				try (SwapFileWriter writer = swapFiles.addLast()) {
					for (int i = 0; i < tailCount; i++) {
						writer.write(queue.removeLast());
					}
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}

			}

			// ホッパーの状態が変わったので通知
			lock.notifyAll();

		}
	}

	/*
	 * 現在位置が0であった場合、スワップファイルがない場合は、
	 * キューにアイテムが追加されるまで待機し、Popの動作をやり直す。
	 * スワップファイルがある場合、スワップファイルを展開してキューの出口から挿入し、
	 * 現在位置をスワップ化サイズにし、現在位置が0でない場合の処理に進む。
	 * 現在位置が0でない場合、キューからアイテムを出し、現在位置を1減らす。
	 * その後、スワップファイルがない場合は、現在位置の更新を行う。
	 */
	/**
	 * terminated状態の時に呼び出した場合、nullを返します。
	 */
	@Override
	public Deque<I> pop(int amount) throws InterruptedException
	{
		synchronized (lock) {

			while (true) {

				// このホッパーはもうアイテムを取り出すことができない
				if (isTerminated) return null;
				if (isEmpty()) return null;

				// 無条件に取り出せるアイテムがないなら、ファイルを解体するか、アイテムが来るのを待つ
				if (currentPosition == 0) {

					if (swapFiles.size() != 0) {
						// スワップファイルがある

						// スワップファイル解体
						try (SwapFileReader reader = swapFiles.removeFirst()) {
							while (true) {
								Optional<I> oItem = reader.read();
								if (!oItem.isPresent()) break;
								queue.addFirst(oItem.get());
								currentPosition++;
							}
						} catch (IOException e) {
							e.printStackTrace(); // TODO エラー処理
						}

						// スワップファイルがない場合、現在位置を更新する
						updateCurrentPosition();

					} else {
						// スワップファイルがない

						lock.wait();

						continue;
					}

				}

				// 無条件に取り出せるアイテムがある

				amount = Math.min(amount, currentPosition);

				// 掬う
				Deque<I> result = popImpl(amount);
				plusItemCountProcessing(result.size());

				currentPosition -= result.size();

				// スワップファイルがない場合、現在位置を更新する
				updateCurrentPosition();

				// ホッパーの状態が変わったので通知
				lock.notifyAll();

				return result;
			}

		}
	}

	/*
	 * スワップファイルがない場合、
	 * 現在位置を、キューサイズがスワップサイズ以下ならばキューサイズ、それ以上ならスワップサイズにする。
	 * 更に、スワップファイルのインデックスをリセットする。
	 */
	/**
	 * このメソッドはロック状態で呼び出さなければならない。
	 */
	protected void updateCurrentPosition()
	{
		if (swapFiles.size() == 0) {
			currentPosition = Math.min(queue.size(), swapSize);
			swapFiles.min = 0;
			swapFiles.max = -1;
		}
	}

	/**
	 * ファイルシステムからスワップファイルを読み込んで初期化します。
	 */
	public void init()
	{
		synchronized (lock) {
			Tuple<Optional<Long>, Optional<Long>> tuple = ISuppliterator.ofObjArray(dir.listFiles())
				.filter(file -> file.isFile())
				.mapIfPresent(file -> swapFiles.parse(file.getName()))
				.apply(ISuppliterator::sorted)
				.collects(
					SuppliteratorCollectors.min(),
					SuppliteratorCollectors.max());
			if (tuple.x.isPresent()) {
				swapFiles.min = tuple.x.get();
				swapFiles.max = tuple.y.get();
			} else {
				swapFiles.min = 0;
				swapFiles.max = -1;
			}
		}
	}

	/**
	 * 現在キューにたまっているアイテムをすべてスワップファイルに書き出し、ホッパーをterminatedにします。
	 * このメソッドはホッパーを閉じませんが、terminatedになったホッパーは、pushおよびpopの操作が不可能になります。
	 * このメソッドの呼び出し後に{@link #push(Object)}を行うと、{@link IllegalStateException}が発生します。
	 * このメソッドの呼び出し後に{@link #pop(int)}を行うと、nullが返されます。
	 */
	public void term()
	{
		synchronized (lock) {

			// スワップファイルより前にアイテムが存在する
			if (currentPosition > 0) {

				// キューの先頭から取り出してリストに末尾から入れる
				// リストは元の順序を維持している
				List<I> list = new ArrayList<>();
				for (int i = 0; i < currentPosition; i++) {
					list.add(queue.removeFirst());
				}
				currentPosition = 0;

				// リストの末尾から取り出し、ライターに食べさせる
				try (SwapFileWriter writer = swapFiles.addFirst()) {
					for (int i = list.size() - 1; i >= 0; i--) {
						writer.write(list.get(i));
					}
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}

			}

			// スワップファイルより後にアイテムが存在する
			if (queue.size() > 0) {

				// キューの末尾からスワップファイルサイズの分だけ取り出し、ライターに食べさせる
				try (SwapFileWriter writer = swapFiles.addLast()) {
					while (!queue.isEmpty()) {
						writer.write(queue.removeLast());
					}
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}

			}

			updateCurrentPosition();

			isTerminated = true;

			// ホッパーの状態が変わったので通知
			lock.notifyAll();

		}
	}

	//

	public abstract void write(OutputStream out, I item) throws IOException;

	/**
	 * これ以上読み込めるものがなかった場合、empty。
	 */
	public abstract Optional<I> read(InputStream in) throws IOException;

	//

	protected class SwapFileWriter implements AutoCloseable
	{

		private final OutputStream out;

		public SwapFileWriter(File file) throws FileNotFoundException
		{
			out = UtilsFile.getOutputStreamWithMkdirs(file);
		}

		public void write(I item) throws IOException
		{
			HopperSwapping.this.write(out, item);
		}

		@Override
		public void close() throws IOException
		{
			out.close();
		}

	}

	protected class SwapFileReader implements AutoCloseable
	{

		private final File file;
		private final InputStream in;

		public SwapFileReader(File file) throws FileNotFoundException
		{
			this.file = file;
			in = new FileInputStream(file);
		}

		/**
		 * これ以上読み込めるものがなかった場合、empty。
		 */
		public Optional<I> read() throws IOException
		{
			return HopperSwapping.this.read(in);
		}

		@Override
		public void close() throws IOException
		{
			in.close();
			file.delete();
		}

	}

	protected class SwapFiles
	{

		private long min = 0;

		/**
		 * この値を含む。
		 * 項目が1個もない場合、minより1小さい値を持つ。
		 */
		private long max = -1;

		public int size()
		{
			return (int) (max - min + 1);
		}

		public SwapFileWriter addFirst() throws FileNotFoundException
		{
			min--;
			return new SwapFileWriter(getSwapFile(min));
		}

		public SwapFileWriter addLast() throws FileNotFoundException
		{
			max++;
			return new SwapFileWriter(getSwapFile(max));
		}

		public SwapFileReader removeFirst() throws FileNotFoundException
		{
			try {
				return new SwapFileReader(getSwapFile(min));
			} finally {
				min++;
			}
		}

		private final Pattern PATTERN = Pattern.compile("(-?[0-9]+)\\.txt");

		public Optional<Long> parse(String fileName)
		{
			Matcher matcher = PATTERN.matcher(fileName);
			if (matcher.matches()) {
				return Optional.of(Long.parseLong(matcher.group(1)));
			} else {
				return Optional.empty();
			}
		}

		public File getSwapFile(long index)
		{
			return new File(dir, "" + index + ".txt");
		}

	}

}
