package pablo.ad.recuperacionllamadascarmelo.model;

public class Llamada implements Comparable<Llamada>{

    private String fechaHora;
    private String numero;
    private String nombre;


    public Llamada(String fechaHora, String numero, String nombre) {
        this.fechaHora = fechaHora;
        this.numero = numero;
        this.nombre = nombre;
    }

    public Llamada() {
        this.fechaHora = "";
        this.numero = "";
        this.nombre = "";
    }


    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    //------------------------------------------------------------


    public String toCsvInterna(){
        return this.fechaHora + "; " + this.numero + "; " + this.nombre + "; ";
    }


    public String toCsvExterna(){
        return this.nombre + "; " + this.fechaHora + "; " + this.numero + "; ";
    }


    @Override
    public int compareTo(Llamada call) {

        int sort = this.nombre.compareTo(call.nombre);
        if(sort == 0) {
            sort = this.fechaHora.compareTo(call.fechaHora);
        }
        return sort;
    }

    @Override
    public String toString() {
        return "Llamada{" +
                "fechaHora='" + fechaHora + '\'' +
                ", numero='" + numero + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
