package gatheringAI;

import java.awt.Point;
import java.util.LinkedList;

import baseAI.BaseAgent;
import map.Map;
import resources.Drone;
import util.Position;

public class HarDrone extends Drone {
	private boolean hasHarvested = false;
	BaseAgent base = new BaseAgent("Allan");
	LinkedList<Point> pathOut = new LinkedList<Point>();
	LinkedList<Point> pathHome = new LinkedList<Point>();
	
	public HarDrone(Map map, Point position) {
		super(map, position);
	}
	
	@Override
	protected void doRun() {
		while(true) {
			synchronized (map.render) {
				try {
					map.render.wait();
					move(route.getFirst());
					route.removeFirst();
				} catch (InterruptedException e) {
					
				}
			}
			//move(0)1
		}
	}
	
	public void getPathOut(){
		this.pathOut = base.sendHarvester();
	}

	public void getPathHome(){
		this.pathHome = base.sendHarvester();
	}
}
