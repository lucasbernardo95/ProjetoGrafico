package beans;

/**
 *
 * @author lucas
 */
public class Dados {

    private Double dado;
    private Double desvio;
    private int frequencia;
    private boolean contado = false;

    public Dados(Double dado, Double desvio) {
        this.dado = dado;
        this.desvio = desvio;
    }

    public Double getDado() {
        return dado;
    }

    public Double getDesvio() {
        return desvio;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    public boolean isContado() {
        return contado;
    }

    public void setContado(boolean contado) {
        this.contado = contado;
    }

}
