import Objetos.*;
import javax.swing.JFrame;

public static void main(String[] args) {

    JFrame janela = new JFrame("Meu Jogo");

    TelaDeFundo painelDoJogo = new TelaDeFundo();
    janela.add(painelDoJogo);

    janela.setSize(800, 600);
    janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    janela.setVisible(true);
}