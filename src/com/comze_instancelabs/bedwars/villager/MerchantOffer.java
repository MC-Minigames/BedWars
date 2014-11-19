package com.comze_instancelabs.bedwars.villager;

import org.bukkit.inventory.ItemStack;

import com.comze_instancelabs.bedwars.villager.ReflectionUtils.NMSMerchantRecipe;
import com.comze_instancelabs.bedwars.villager.ReflectionUtils.OBCCraftItemStack;

public class MerchantOffer {
	
	private ItemStack[] items = new ItemStack[3];
	
	public MerchantOffer(ItemStack is1, ItemStack is2, ItemStack re) {
		this.items[0] = is1;
		this.items[1] = is2;
		this.items[2] = re;
	}
	
	public MerchantOffer(ItemStack is, ItemStack re) {
		this(is, null, re);
	}
	
	protected MerchantOffer(NMSMerchantRecipe handle) {
		this.items[0] = OBCCraftItemStack.asBukkitCopy(handle.getBuyItem1());
		this.items[1] = (handle.getBuyItem2() == null) ? null : OBCCraftItemStack.asBukkitCopy(handle.getBuyItem2());
		this.items[2] = OBCCraftItemStack.asBukkitCopy(handle.getBuyItem3());
	}
	
	protected NMSMerchantRecipe getHandle() {
		if (this.items[1] == null)
			return new NMSMerchantRecipe(OBCCraftItemStack.asNMSCopy(this.items[0]), OBCCraftItemStack.asNMSCopy(this.items[2]));
		else
			return new NMSMerchantRecipe(OBCCraftItemStack.asNMSCopy(this.items[0]), OBCCraftItemStack.asNMSCopy(this.items[1]), OBCCraftItemStack.asNMSCopy(this.items[2]));
	}
	
	public ItemStack getFirstInput() {
		return this.items[0];
	}
	
	public ItemStack getSecondInput() {
		return this.items[1];
	}
	
	public ItemStack getOutput() {
		return this.items[2];
	}
	
}
