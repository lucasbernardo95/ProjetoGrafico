package beans;

/**
 *
 * @author lucas
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

@ManagedBean(name = "bar")
public class Graficos implements Serializable {

    /**
     * Fontes http://pt.wikihow.com/Calcular-Desvio-Padr%C3%A3o
     * http://pt.wikihow.com/Calcular-a-Amplitude
     * http://www.portalaction.com.br/estatistica-basica/22-medidas-de-dispersao
     */
    private BarChartModel barraGrafico;
    private LineChartModel linhaGrafico;
    
    private int barra1, barra2, barra3, barra4, barra5,
            barra6, barra7, barra8, barra9;
    private Double valor;
    private Double media;
    private Double amplitude;
    private Double variancia;
    private Double desvioPadrao;
    private int total;
    private int somaFrequencias;
    private List<Dados> listTable = new ArrayList<>();
    private List<Dados> frequencia = new ArrayList<>();
    private final String caminho = "C:\\Users\\lber\\Desktop\\estatistica.txt";
    
    /*@PostConstruct
    public void init(){
        
    }*/

    //Método para ler os dados, calcular a média e amplitude
    public void lerDados() throws IOException {
        try {
            FileReader arquivo = new FileReader(caminho);
            if (arquivo.equals("")) {
                System.out.println("void");
                return;
            }
            BufferedReader LerArquivo = new BufferedReader(arquivo);

            Double auxMedia = 0.0, maior = 0.0, menor = 500.0;;
            int tt = 0;
            do {

                valor = Double.parseDouble(LerArquivo.readLine());
                tt++;

                if (valor >= 86.60 && valor <= 89.90) {
                    ++barra1;
                } else if (valor >= 90.60 && valor <= 93.90) {
                    ++barra2;
                } else if (valor >= 94.60 && valor <= 97.90) {
                    ++barra3;
                } else if (valor >= 98.60 && valor <= 101.90) {
                    ++barra4;
                } else if (valor >= 102.60 && valor <= 105.90) {
                    ++barra5;
                } else if (valor >= 106.60 && valor <= 109.90) {
                    ++barra6;
                } else if (valor >= 110.60 && valor <= 113.90) {
                    ++barra7;
                } else if (valor >= 114.60 && valor <= 117.90) {
                    ++barra8;
                } else {
                    ++barra9;
                }
                auxMedia += valor;

                //pegando o menor e maior valor para efetuar o calculo da amplitude
                if (valor < menor) {
                    menor = valor;
                }
                if (valor > maior) {
                    maior = valor;
                }

            } while (LerArquivo.readLine() != null);

            media = auxMedia / tt;
            amplitude = maior - menor;
            setTotal(tt);
            arquivo.close();

            desvioPadrão();//calcula o desvio padrão
            frequenciaAcumulada();//chama o método da frequencia acumulada
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Graficos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Calcula a frequência acumulada
    public void frequenciaAcumulada() {
        int aux = 0;
        for (int i = 0; i < listTable.size() - 1; i++) {
            int c = 1;

            for (int j = (i + 1); j < (listTable.size()); j++) {
                if (Objects.equals(listTable.get(i).getDado(), listTable.get(j).getDado()) && !listTable.get(i).isContado()) {
                    listTable.get(j).setContado(true);
                    ++c;
                }
            }

            if (!listTable.get(i).isContado()) {
                listTable.get(i).setContado(true);//indica que esse número já foi contado pra não repetir o processo de contagem
                listTable.get(i).setFrequencia(c);//recebe o número de vezes que esse se repetiu
                frequencia.add(listTable.get(i));
            }
            c = 0;//zera a variável contadora para iniciar uma nova contagem
            
        }for (int j = 0; j < frequencia.size(); j++) {
            aux += frequencia.get(j).getFrequencia();
        }
        somaFrequencias = aux;
    }

    //Método para calcular o desvio padrão e variancia
    private void desvioPadrão() throws IOException {
        Double v = 0.0;
        Double o = 0.0;
        int t = getTotal() - 1;// variável que representa o total - 1 para efetuar o calculo da variância e desvio padrão amostral
        try {
            FileReader arquivo = new FileReader(caminho);
            BufferedReader LerArquivo = new BufferedReader(arquivo);
            do {
                valor = Double.parseDouble(LerArquivo.readLine());

                //Calculando a variancia  http://www.portalaction.com.br/estatistica-basica/22-medidas-de-dispersao
                v += Math.pow((valor - media), 2) / t; //Variância amostral
                // desvio padrão amostral               
                o += Math.sqrt(  Math.pow(valor - media, 2) / t);
                
                //Objeto que recebe cada valor contido no arquivo txt e o desvio padrão de cada um desses valores
                Dados dado = new Dados(valor, (media - valor));

                listTable.add(dado);
            } while (LerArquivo.readLine() != null);
            arquivo.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Graficos.class.getName()).log(Level.SEVERE, null, ex);
        }
        variancia = v;
        desvioPadrao = o;
    }

    //Método resposável por setar legendas e valores de cada barra do gráfico
    private BarChartModel iniciarBarra() throws IOException {
        lerDados();//Faz a leitura dos dadas e os divide em classes "que é representado por um tipo inteiro chamado 'barra'"

        BarChartModel model = new BarChartModel();

        ChartSeries barra = new ChartSeries();
        barra.setLabel("wheel-base");
        barra.set("86.60 à 89.90", (Number) barra1);
        barra.set("90.60 à 93.90", (Number) barra2);
        barra.set("94.60 à 97.90", (Number) barra3);
        barra.set("98.60 à 101.90", (Number) barra4);
        barra.set("102.60 à 105.90", (Number) barra5);
        barra.set("106.60 à 109.90", (Number) barra6);
        barra.set("110.60 à 113.90", (Number) barra7);
        barra.set("114.60 à 117.90", (Number) barra8);
        barra.set("118.60 à 121.90", (Number) barra9);

        model.addSeries(barra);

        return model;
    }
    
    //Método responsável por informar qual a posição do gráfico, tamanho da tabela do mesmo e legendas dos eixos x e y
    public void criarBarra() throws IOException {
        barraGrafico = iniciarBarra();

        barraGrafico.setTitle("Histograma");
        barraGrafico.setLegendPosition("ne");

        Axis eixoX = barraGrafico.getAxis(AxisType.X);
        

        Axis eixoY = barraGrafico.getAxis(AxisType.Y);
        eixoY.setLabel("Quantidade");
        eixoY.setMin(0);
        eixoY.setMax(120);
        
        criarLinha();
        
    }

    private LineChartModel iniciarLinha() {
        LineChartModel linha = new LineChartModel();

        ChartSeries dado = new ChartSeries();
        dado.setLabel("wheel-base");
        dado.set("86.60 à 89.90", (Number) barra1);
        dado.set("90.60 à 93.90", (Number) barra2);
        dado.set("94.60 à 97.90", (Number) barra3);
        dado.set("98.60 à 101.90", (Number) barra4);
        dado.set("102.60 à 105.90", (Number) barra5);
        dado.set("106.60 à 109.90", (Number) barra6);
        dado.set("110.60 à 113.90", (Number) barra7);
        dado.set("114.60 à 117.90", (Number) barra8);
        dado.set("118.60 à 121.90", (Number) barra9);

        linha.addSeries(dado);
         
        return linha;
    }
    
    private void criarLinha() {
        linhaGrafico = iniciarLinha();
        linhaGrafico.setTitle("Frequência acumulada");
        linhaGrafico.setLegendPosition("e");
        linhaGrafico.setShowPointLabels(true);
        linhaGrafico.getAxes().put(AxisType.X, new CategoryAxis("Years"));
        Axis yAxis = linhaGrafico.getAxis(AxisType.Y);
        yAxis = linhaGrafico.getAxis(AxisType.Y);
        yAxis.setLabel("Births");
        yAxis.setMin(0);
        yAxis.setMax(200);
    }
    
    public BarChartModel getBarraGrafico() {
        return barraGrafico;
    }

    public Double getMedia() {
        return media;
    }

    public Double getAmplitude() {
        return amplitude;
    }

    public List<Dados> getListTable() {
        return listTable;
    }

    public void setListTable(List<Dados> listTable) {
        this.listTable = listTable;
    }

    public Double getVariancia() {
        return variancia;
    }

    public List<Dados> getFrequencia() {
        return frequencia;
    }

    public Double getDesvioPadrao() {
        return desvioPadrao;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSomaFrequencias() {
        return somaFrequencias;
    }

    public LineChartModel getLinhaGrafico() {
        return linhaGrafico;
    }
}
