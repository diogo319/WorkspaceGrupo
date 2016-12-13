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
		System.out.println("Buffer inserir");
		try {
			elementosLivres.acquire();
			acessoElemento.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		buffer[putBuffer] = i;
		putBuffer = ++putBuffer % dim;
		elementosOcupados.release();
		acessoElemento.release();
	}
	
	public int ler() {
		System.out.println("Buffer ler");
		try {
			elementosOcupados.acquire();
			acessoElemento.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = buffer[getBuffer];
		getBuffer = ++getBuffer % dim;
		acessoElemento.release();
		elementosLivres.release();
		return i;
	}
	
	
	
	public int elementosLivres() {
		return elementosLivres.availablePermits();
	}

}
