package com.comze_instancelabs.bedwars.sheep;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.server.v1_7_R4.AttributeInstance;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntitySheep;
import net.minecraft.server.v1_7_R4.GenericAttributes;
import net.minecraft.server.v1_7_R4.Navigation;
import net.minecraft.server.v1_7_R4.World;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;

public class Sheeep1710 extends EntitySheep implements Sheeep {

	public Sheeep1710(World world, Entity target) {
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
		this.goalSelector.a(0, new PathfinderGoalMeleeAttack1710(this, EntityHuman.class, 1D, false));
		// this.goalSelector.a(0, new PathfinderGoalFollowParent(this, 1.1D));
		this.setTarget(target);
		((Sheep) this.getBukkitEntity()).setTarget((LivingEntity) target.getBukkitEntity());
	}

	public Location getLocation() {
		return new Location(this.world.getWorld(), this.locX, this.locY, this.locZ);
	}

}
