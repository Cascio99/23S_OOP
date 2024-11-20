package main;
//player, enemy의 움직임을 위한 인터페이스
public interface Movable {
	public abstract void left();
	public abstract void right();
	public abstract void up();
	public abstract void down();
}
