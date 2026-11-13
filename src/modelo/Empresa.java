package modelo;

// Empresa que vai ser avaliada
public class Empresa {

    private int id;
    private String razaoSocial;
    private String cnpj;
    private String setor;
    private String porte;
    private String cidadeUf;

    public Empresa(int id, String razaoSocial, String cnpj, String setor, String porte, String cidadeUf) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.setor = setor;
        this.porte = porte;
        this.cidadeUf = cidadeUf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getSetor() {
        return setor;
    }

    public String getPorte() {
        return porte;
    }

    public String getCidadeUf() {
        return cidadeUf;
    }
}
