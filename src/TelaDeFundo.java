import Objetos.*;

import java.awt.*;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TelaDeFundo extends JPanel implements ActionListener, KeyListener {
private GerenciadorBanco banco = new GerenciadorBanco();
    private Timer timer;
    private Chest ch;
    private bomberman bomberman;
    private boolean jogoVencido = false;
    private List<bomba> bombas = new ArrayList<>();
    private List<blocoEstavel> blocosIndestrutivos = new ArrayList<>();
    private List<Caixa> caixas = new ArrayList<>();
    private int contador = 3;
    private int[][] mapa = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,2,0,0,0,2,0,0,0,0,2,2,2,0,1},
            {1,2,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
            {1,0,2,0,0,2,0,0,0,2,0,0,2,0,0,0,0,0,1},
            {1,0,1,2,1,0,1,0,1,0,1,2,1,2,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,0,1},
            {1,0,1,0,1,0,1,0,1,2,1,0,1,2,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,2,0,2,0,0,0,0,1},
            {1,2,1,0,1,0,1,0,1,0,1,0,1,2,1,2,1,0,1},
            {1,0,0,2,0,0,0,2,0,0,2,0,2,0,2,3,2,0,1},
            {1,0,1,2,1,0,1,0,1,0,1,0,1,0,1,2,1,0,1},
            {1,2,0,2,2,0,0,2,0,0,2,0,0,2,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    };

    public TelaDeFundo() {
        this.bomberman = new bomberman(125, 100, 3, 30, 30);
        timer = new Timer(16, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        criarMapa();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<bomba> bombasQueExplodiram = new ArrayList<>();
        Iterator<bomba> iterBombas = bombas.iterator();
        while (iterBombas.hasNext()) {
            bomba b = iterBombas.next();
            b.update(16);

            if (b.acaboudDeExplodir()) {
                bombasQueExplodiram.add(b);
            }
            if (b.estaProntaParaRemover()) {
                iterBombas.remove();
            }
        }

        for (bomba explosao : bombasQueExplodiram) {
            Rectangle areaDaExplosao = explosao.getAreaDeExplosao();
            Iterator<Caixa> iterCaixas = caixas.iterator();
            while (iterCaixas.hasNext()) {
                Caixa caixa = iterCaixas.next();
                Rectangle retanguloCaixa = new Rectangle(caixa.getX(), caixa.getY(), caixa.getLargura(), caixa.getAltura());
                if (areaDaExplosao.intersects(retanguloCaixa)) {
                    iterCaixas.remove();
                }
            }
            explosao.processouExplosao();
        }

        if (!jogoVencido) {
            verificarVitoria();
        }

        repaint();
    }


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F5) {
            banco.salvarJogo(bomberman.getX(), bomberman.getY(), contador, caixas);
        }

        if (e.getKeyCode() == KeyEvent.VK_F9) {
            GerenciadorBanco.SaveData save = banco.carregarJogo();
            if (save != null) {
                bomberman.setX(save.x);
                bomberman.setY(save.y);
                contador = save.bombas;
                restaurarCaixas(save.caixas);
                repaint();
                System.out.println("JOGO CARREGADO!");
            }
        }
        if (jogoVencido) {
            return;
        }

        int proximoX = bomberman.getX();
        int proximoY = bomberman.getY();
        int velocidade = 10;

        if (e.getKeyCode() == KeyEvent.VK_D) {
            proximoX += velocidade;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            proximoX -= velocidade;
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            proximoY -= velocidade;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            proximoY += velocidade;
        } else if (e.getKeyCode() == KeyEvent.VK_E && contador >= 1) {

                bomba novaBomba = new bomba(bomberman.getX(), bomberman.getY(), 40, 40);
                novaBomba.plantar();
                bombas.add(novaBomba);
                contador--;
                return;

        }

        Rectangle bombermanFuturo = new Rectangle(proximoX, proximoY, bomberman.getLargura(), bomberman.getAltura());
        boolean vaiColidir = false;

        for (blocoEstavel bloco : blocosIndestrutivos) {
            Rectangle retanguloBloco = new Rectangle(bloco.getX(), bloco.getY(), bloco.getLargura(), bloco.getAltura());
            if (bombermanFuturo.intersects(retanguloBloco)) {
                vaiColidir = true;
                break;
            }
        }
        if (!vaiColidir) {
            for (Caixa caixa : caixas) {
                Rectangle retanguloCaixa = new Rectangle(caixa.getX(), caixa.getY(), caixa.getLargura(), caixa.getAltura());
                if (bombermanFuturo.intersects(retanguloCaixa)) {
                    vaiColidir = true;
                    break;
                }
            }
        }

        if (!vaiColidir) {
            bomberman.setX(proximoX);
            bomberman.setY(proximoY);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private void criarMapa() {
        for (int linha = 0; linha < mapa.length; linha++) {
            for (int coluna = 0; coluna < mapa[linha].length; coluna++) {
                int codigoDoObjeto = mapa[linha][coluna];
                int posX = coluna * 40;
                int posY = linha * 40;

                switch (codigoDoObjeto) {
                    case 1:
                        blocosIndestrutivos.add(new blocoEstavel(posX, posY));
                        break;
                    case 2:
                        caixas.add(new Caixa(posX, posY));
                        break;
                    case 3:
                        ch = new Chest(posX, posY);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GREEN);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.drawImage(bomberman.getImagem(), bomberman.getX(), bomberman.getY(), bomberman.getLargura(), bomberman.getAltura(), null);

        for (bomba b : bombas) {
            b.draw(g);
        }

        if (ch != null) {
            g.drawImage(ch.getImagem(), ch.getX(), ch.getY(), ch.getLargura(), ch.getAltura(), null);
        }
        for (blocoEstavel blocos : blocosIndestrutivos) {
            g.drawImage(blocos.getImagem(), blocos.getX(), blocos.getY(), blocos.getLargura(), blocos.getAltura(), null);
        }
        for (Caixa caixa : caixas) {
            g.drawImage(caixa.getImagem(), caixa.getX(), caixa.getY(), caixa.getLargura(), caixa.getAltura(), null);
        }
        if (jogoVencido) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.WHITE);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 50));

            String msgVitoria = "Você Venceu!";
            int strWidth = g.getFontMetrics().stringWidth(msgVitoria);
            g.drawString(msgVitoria, (getWidth() - strWidth) / 2, getHeight() / 2);
        }
        Font fonteGrossa = new Font("Arial", Font.BOLD, 18);
        g.setFont(fonteGrossa);
        g.setColor(Color.WHITE);
        g.drawString("Bombas: "+contador, 650, 25);

    }

    private void verificarVitoria() {
        Rectangle rBomberman = new Rectangle(bomberman.getX(), bomberman.getY(), bomberman.getLargura(), bomberman.getAltura());

        if (ch != null) {
            Rectangle rChest = new Rectangle(ch.getX(), ch.getY(), ch.getLargura(), ch.getAltura());

            if (rBomberman.intersects(rChest)) {
                jogoVencido = true;
                timer.stop();
                System.out.println("Você Venceu!");
            }
        }
    }
    private void restaurarCaixas(String dados) {
        if (dados == null || dados.isEmpty()) return;

        caixas.clear();
        String[] pares = dados.split(";");

        for (String par : pares) {
            if (!par.trim().isEmpty()) {
                String[] coords = par.split(",");
                if (coords.length == 2) {
                    int cx = Integer.parseInt(coords[0]);
                    int cy = Integer.parseInt(coords[1]);
                    caixas.add(new Caixa(cx, cy));
                }
            }
        }
    }
}