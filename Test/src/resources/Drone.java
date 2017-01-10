package resources;

import java.awt.Point;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

import map.*;
import resources.*;

public class Drone extends Agent {
	
	Map map;
	protected String TYPE;
	Point position = new Point();
	
	public Drone(Map map, Point position) {
		super(UUID.randomUUID().toString());
		this.map = map;
		this.position = position;
	}
	
	protected void doRun() {
		while(true) {
			synchronized (map.render) {
				try {
					map.render.wait();
				} catch (InterruptedException e) {
					
				}
			}
			move(0);			
		}
	}
	
	private void move(int dir) {
		try {
			Template template = new Template(new ActualTemplateField(TYPE),
					new ActualTemplateField(position.x),
					new ActualTemplateField(position.y));
			Tuple t = get(template, Self.SELF);
			int[] xy = getDirection(dir, position.x, position.y);
			Template template2 = getDirectionTemplate(xy);
			position.move(xy[0], xy[1]);
			Tuple t2 = new Tuple(TYPE, xy[0], xy[1]);
			put(t2, Self.SELF);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Template getDirectionTemplate(int[] arr){
		return new Template( Map.AnyString,
								 new ActualTemplateField(arr[0]),
								 new ActualTemplateField(arr[1]) );
	}
	
	private static int[] getDirection(int dir, int x, int y){
		switch(dir){
			case 0: x-=1; break; // LEFT
			case 1: x+=1; break; // RIGHT
			case 2: y-=1; break; // UP
			case 3: y+=1; break; // DOWN
		}
		return new int[]{x,y};
	}
}

