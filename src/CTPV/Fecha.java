package CTPV;

public class Fecha {
    
    private int horas;
    private int minutos;
    private int dia;
    private int mes;
    private int año;

    public Fecha(int horas, int minutos, int dia, int mes, int año) {
        this.horas = horas;
        this.minutos = minutos;
        this.dia = dia;
        this.mes = mes;
        this.año = año;
    }

    public Fecha() {
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    @Override
    public String toString() {
        return dia+"/"+mes+"/"+año+" - "+horas+":"+minutos;
    }
    
    
    
    
    
}
