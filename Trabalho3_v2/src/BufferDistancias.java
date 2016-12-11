import java.util.concurrent.Semaphore;

public class BufferDistancias {
	final int dim = 2;
	int[] buffer;
	int putBuffer,getBuffer;
	Semaphore elementosLivres,acessoElemento,elementosOcupados;
	
	public BufferDistancias(){
		buffer = new int[dim];
		putBuffer = 0;
		getBuffer = 0;
		elementosLivres = new Semaphore(dim);
		acessoElemento = new Semaphore(1);
		elementosOcupados = new Semaphore(0);
	}
	
	public void inserir(int i) {
		try {
			elementosLivres.acquire();
			acessoElemento.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		putBuffer = ++putBuffer % dim;
		buffer[putBuffer] = i;
		elementosOcupados.release();
		acessoElemento.release();
	}
	
	public int remover() {
		try {
			elementosOcupados.acquire();
			acessoElemento.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getBuffer = ++getBuffer % dim;
		int i = buffer[getBuffer];
		acessoElemento.release();
		elementosLivres.release();
		return i;
	}

}
