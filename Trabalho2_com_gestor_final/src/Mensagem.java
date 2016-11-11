public enum Mensagem {
	GUI,EVITAR,VAGUEAR,EVITAR_TERMINADO,VAGUEAR_TERMINADO,GESTOR_TERMINADO,TERMINAR;
	
	@Override
    public String toString() {
        return "Comunicação: " + this.name();
    }
};

