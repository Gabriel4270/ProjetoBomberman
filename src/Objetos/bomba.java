package Objetos;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;


public class bomba {
    private int x;
    private int y;
    private int largura;
    private int altura;
    private BufferedImage imagem;
    private boolean estaPlantada = false;
    private boolean explodiu = false;
    private final long TEMPO_DE_FUSAO = 3000;
    private long tempoDesdePlantada = 0;
    private final long DURACAO_DA_EXPLOSAO = 500;
    private long tempoNaExplosao = 0;
    private boolean prontaParaRemover = false;
    private boolean acabouDeExplodir = false;

    public bomba(int x, int y, int largura, int altura) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        try {
            imagem = ImageIO.read(getClass().getResourceAsStream("/bomba.png"));
        } catch (IOException e) {
            System.err.println("Erro ao carregar a imagem da Bomba!");
            e.printStackTrace();
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getAltura() { return altura; }
    public int getLargura() { return largura; }
    public BufferedImage getImagem() { return imagem; }

    public void plantar() {
        this.estaPlantada = true;
    }

    public boolean estaProntaParaRemover() {
        return prontaParaRemover;
    }

    public boolean acaboudDeExplodir() {
        return acabouDeExplodir;
    }
    public void processouExplosao() {
        this.acabouDeExplodir = false;
    }
    public Rectangle getAreaDeExplosao() {
        return new Rectangle(this.x - 20, this.y - 20, this.largura + 40, this.altura + 40);
    }

    public void update(long deltaTime) {
        if (!estaPlantada) {
            return;
        }

        if (explodiu) {
            tempoNaExplosao += deltaTime;
            if (tempoNaExplosao >= DURACAO_DA_EXPLOSAO) {
                prontaParaRemover = true;
            }
        } else {
            tempoDesdePlantada += deltaTime;
            if (tempoDesdePlantada >= TEMPO_DE_FUSAO) {
                explodiu = true;
                acabouDeExplodir = true;
            }
        }
    }

    public void draw(Graphics g) {
        if (!estaPlantada || prontaParaRemover) {
            return;
        }

        if (explodiu) {
            Rectangle area = getAreaDeExplosao();
            g.setColor(Color.YELLOW);
            g.fillRect(area.x, area.y, area.width, area.height);
        } else {
            g.drawImage(this.imagem, this.x, this.y, this.largura, this.altura, null);
        }
    }
}