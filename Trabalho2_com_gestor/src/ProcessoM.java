import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


public class ProcessoM {
	private File file;
	private FileChannel channel;
	private MappedByteBuffer buffer;
	private final int BUFFER_MAX = 30;
	private RandomAccessFile raf;
		
	public ProcessoM() {
		file = new File("comunicacao.dat");
		try {
			raf = new RandomAccessFile(file, "rw");
			channel = raf.getChannel();
		} catch (Exception e) { e.printStackTrace(); }
		try {
			buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0,BUFFER_MAX);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public Mensagem receberMensagem() {
		buffer.position(0);
		return Mensagem.values()[buffer.getInt()];
	}
	
	public void enviarMensagem(Mensagem message) {
		buffer.position(0);
		buffer.putInt(message.ordinal());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fecharCanal();
	}
	
	public void fecharCanal() {
		try {
			channel.close();
			raf.close();
		} catch (Exception e) { e.printStackTrace(); };
	}
	
	public boolean esperarMensagem(Mensagem msg) {
		Mensagem recebida = receberMensagem();
		while(msg != recebida) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			recebida = receberMensagem();
		}
		return true;
	}
	
	public static void main(String[] args) {
		
	}
	
}
