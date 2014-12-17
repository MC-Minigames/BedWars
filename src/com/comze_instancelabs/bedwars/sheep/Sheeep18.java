package com.comze_instancelabs.bedwars.sheep;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.AttributeInstance;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.EntitySheep;
import net.minecraft.server.v1_8_R1.Navigation;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;

public class Sheeep18 extends EntitySheep implements Sheeep {

	public Sheeep18(World world, Entity target) {
		super(world);
		try {
			Field b = this.goalSelector.getClass().getDeclaredField("b");
			b.setAccessible(true);
			b.set(this.goalSelector, new ArrayList());
			Field field = Navigation.class.getDeclaredField("e");
			field.setAccessible(true);
			AttributeInstance ai = (AttributeInstance) field.get(this.getNavigation());
			ai.setValue(128);
			this.getAttributeInstance(GenericAttributes.b).setValue(128D);
			this.getAttributeInstance(GenericAttributes.d).setValue(0.37D);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.goalSelector.a(0, new PathfinderGoalMeleeAttack18(this, EntityHuman.class, 1D, false));
		// this.goalSelector.a(0, new PathfinderGoalFollowParent(this, 1.1D));
		this.setGoalTarget((EntityLiving) target);
		((Sheep) this.getBukkitEntity()).setTarget((LivingEntity) target.getBukkitEntity());
	}
	
	public Location getLocation() {
		return new Location(this.world.getWorld(), this.locX, this.locY, this.locZ);
	}

}
