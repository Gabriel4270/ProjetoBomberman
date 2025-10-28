package Objetos;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Caixa {
    private int x;
    private int y;
    private int altura = 40;
    private int largura = 40;
    private BufferedImage imagem;

    public Caixa(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            imagem = ImageIO.read(getClass().getResourceAsStream("/caixa.png"));
        } catch (
                IOException e) {
            System.err.println("Erro ao carregar a imagem do quadrado!");
            e.printStackTrace();
        }
    }
    public int getX() {return x;}
    public int getY() {return y;}
    public int getAltura() {return altura;}
    public int getLargura() {return largura;}
    public BufferedImage getImagem() {return imagem;}
}
