package Objetos;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
public class bomberman {
    private int x;
    private int y;
    private int largura;
    private int altura;
    private int vida = 3;
    private BufferedImage imagem;

    public bomberman(int x, int y, int vida,  int largura, int altura) {
        this.x = x;
        this.y = y;
        this.vida = vida;
        this.largura = largura;
        this.altura = altura;

        try {
            imagem = ImageIO.read(getClass().getResourceAsStream("/bomberman.png"));
        } catch (
                IOException e) {
            System.err.println("Erro ao carregar a imagem do Bomberman!");
            e.printStackTrace();
        }
    }
    public int getX() {return x;}
    public int getY() {return y;}
    public int setX(int x) {this.x = x; return x;}
    public int setY(int y) {this.y = y; return y;}
    public int getLargura() {return largura;}
    public int getAltura() {return altura;}
    public int getVida() {return vida;}
    public BufferedImage getImagem() { return imagem; }


    public bomba plantarBomba(){
        bomba novaBomba = new bomba (this.x, this.y,largura,altura);

        return novaBomba;
    }





}
