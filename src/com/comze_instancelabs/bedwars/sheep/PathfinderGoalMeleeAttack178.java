package com.comze_instancelabs.bedwars.sheep;

import net.minecraft.server.v1_7_R3.Entity;
import net.minecraft.server.v1_7_R3.EntityCreature;
import net.minecraft.server.v1_7_R3.EntityLiving;
import net.minecraft.server.v1_7_R3.PathfinderGoalMeleeAttack;

public class PathfinderGoalMeleeAttack178 extends PathfinderGoalMeleeAttack {

	EntityCreature b;
	
	public PathfinderGoalMeleeAttack178(EntityCreature entitycreature, double d0, boolean flag) {
		super(entitycreature, d0, flag);
		b = entitycreature;
	}
	
	public PathfinderGoalMeleeAttack178(EntityCreature entitycreature, Class c, double d0, boolean flag) {
		super(entitycreature, c, d0, flag);
		b = entitycreature;
	}

	@Override
	public void e() {
		b.getNavigation().a(b.getGoalTarget());
    }
	
}
