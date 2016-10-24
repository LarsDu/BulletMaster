package bulletmaster.entity;

import bulletmaster.Animation;
import bulletmaster.collisions.CollidableActor;
import bulletmaster.gfx.SpriteSheet;

public class HealthyActor  extends Actor implements CollidableActor {
	
	//An actor with health, attack power, 
	


	protected int maxHealth = 100;
	protected int currentHealth = 100;
	protected int attackPower = 0;


	

	
	
	
	
	public void die(){
	
	}
	
	public void attack(){
	}
	
	
	
	
	public int getMaxHealth() {
		return maxHealth;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public int getAttackPower() {
		return attackPower;
	}



	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	public void setAttackPower(int attackPower) {
		this.attackPower = attackPower;
	}
	

}
