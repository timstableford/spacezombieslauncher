package cx.it.hyperbadger.spacezombies.launcher;

public interface Progress {
	public void setMax(int max);
	public void setCurrent(int current);
	public void setTitle(String title);
}
