package com.comze_instancelabs.bedwars.sheep;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.PathfinderGoalMeleeAttack;

public class PathfinderGoalMeleeAttack1710 extends PathfinderGoalMeleeAttack {

	EntityCreature b;
	
	public PathfinderGoalMeleeAttack1710(EntityCreature entitycreature, double d0, boolean flag) {
		super(entitycreature, d0, flag);
		b = entitycreature;
	}
	
	public PathfinderGoalMeleeAttack1710(EntityCreature entitycreature, Class c, double d0, boolean flag) {
		super(entitycreature, c, d0, flag);
		b = entitycreature;
	}

	@Override
	public void e() {
		b.getNavigation().a(b.getGoalTarget());
        /*EntityLiving entityliving = this.b.getGoalTarget();

        this.b.getControllerLook().a(entityliving, 30.0F, 30.0F);
        double d0 = this.b.e(entityliving.locX, entityliving.boundingBox.b, entityliving.locZ);
        double d1 = (double) (this.b.width * 2.0F * this.b.width * 2.0F + entityliving.width);

        --this.h;
        if ((this.e || this.b.getEntitySenses().canSee(entityliving)) && this.h <= 0 && (this.i == 0.0D && this.j == 0.0D && this.k == 0.0D || entityliving.e(this.i, this.j, this.k) >= 1.0D || this.b.aI().nextFloat() < 0.05F)) {
            this.i = entityliving.locX;
            this.j = entityliving.boundingBox.b;
            this.k = entityliving.locZ;
            this.h = 4 + this.b.aI().nextInt(7);
            if (d0 > 1024.0D) {
                this.h += 10;
            } else if (d0 > 256.0D) {
                this.h += 5;
            }

            if (!this.b.getNavigation().a((Entity) entityliving, this.d)) {
                this.h += 15;
            }
        }

        this.c = Math.max(this.c - 1, 0);
        if (d0 <= d1 && this.c <= 20) {
            this.c = 20;
            if (this.b.be() != null) {
                this.b.ba();
            }

            this.b.n(entityliving);
        }*/
    }
	
}
