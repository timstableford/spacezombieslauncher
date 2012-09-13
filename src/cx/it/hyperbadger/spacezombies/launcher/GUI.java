package cx.it.hyperbadger.spacezombies.launcher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
public class GUI extends JFrame implements Progress{
	private static final long serialVersionUID = 1L;
	private JProgressBar p;
	public GUI(){
		this.setTitle("SpaceZombies Launcher");
		this.setVisible(true);
		this.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(new Dimension(400,77));
		JPanel panel = new JPanel();
		this.add(panel);
		p = new JProgressBar();
		p.setPreferredSize(new Dimension(380,30));
		p.setStringPainted(true);
		panel.add(p, BorderLayout.CENTER);
		this.pack();
		int w = this.getSize().width;
		int h = this.getSize().height;
		int x = (dim.width/2)-w/2;
		int y = (dim.height/2)-h/2;
		this.setLocation(x, y);
	}

	@Override
	public void setMax(int max) {
		p.setMaximum(max);
	}

	@Override
	public void setCurrent(int current) {
		p.setValue(current);
	}
}
