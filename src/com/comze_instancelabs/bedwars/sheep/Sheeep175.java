package com.comze_instancelabs.bedwars.sheep;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.server.v1_7_R2.AttributeInstance;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntitySheep;
import net.minecraft.server.v1_7_R2.Navigation;
import net.minecraft.server.v1_7_R2.World;
import net.minecraft.server.v1_7_R2.GenericAttributes;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;

public class Sheeep175 extends EntitySheep implements Sheeep {

	public Sheeep175(World world, Entity target) {
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
		this.goalSelector.a(0, new PathfinderGoalMeleeAttack175(this, EntityHuman.class, 1D, false));
		// this.goalSelector.a(0, new PathfinderGoalFollowParent(this, 1.1D));
		this.setTarget(target);
		((Sheep) this.getBukkitEntity()).setTarget((LivingEntity) target.getBukkitEntity());
	}

	public Location getLocation() {
		return new Location(this.world.getWorld(), this.locX, this.locY, this.locZ);
	}
	
}
