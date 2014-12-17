package com.comze_instancelabs.bedwars.sheep;

import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityCreature;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.PathfinderGoalMeleeAttack;

public class PathfinderGoalMeleeAttack175 extends PathfinderGoalMeleeAttack {

	EntityCreature b;
	
	public PathfinderGoalMeleeAttack175(EntityCreature entitycreature, double d0, boolean flag) {
		super(entitycreature, d0, flag);
		b = entitycreature;
	}
	
	public PathfinderGoalMeleeAttack175(EntityCreature entitycreature, Class c, double d0, boolean flag) {
		super(entitycreature, c, d0, flag);
		b = entitycreature;
	}

	@Override
	public void e() {
		b.getNavigation().a(b.getGoalTarget());
    }
	
}
