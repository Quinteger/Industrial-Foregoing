package com.buuz135.industrial.fluid;

import com.buuz135.industrial.utils.ItemStackUtils;
import com.hrznstudio.titanium.util.TagUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.SerializationTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import net.minecraftforge.fluids.FluidAttributes.Builder;

public class OreTitaniumFluidAttributes extends FluidAttributes {

    public static final String NBT_TAG = "Tag";

    public OreTitaniumFluidAttributes(Builder builder, Fluid fluid) {
        super(builder, fluid);
    }

    @Override
    public int getColor(FluidStack stack) {
        if (Minecraft.getInstance().level != null && stack.hasTag() && stack.getTag().contains(NBT_TAG)){
            String tag = stack.getTag().getString(NBT_TAG);
            List<Item> items = SerializationTags.getInstance().getItems().getTagOrEmpty(new ResourceLocation(tag.replace("forge:ores/", "forge:dusts/"))).getValues();
            if (items.size() > 0){
                return ItemStackUtils.getColor(new ItemStack(items.get(0)));
            }
        }
        return super.getColor(stack);
    }

    @Override
    public String getTranslationKey(FluidStack stack) {
        String extra = "";
        if (stack.hasTag() && stack.getTag().contains(NBT_TAG)){
            String tag = stack.getTag().getString(NBT_TAG);
            List<Item> items = SerializationTags.getInstance().getItems().getTagOrEmpty(new ResourceLocation(tag)).getValues();
            if (items.size() > 0){
                extra = " (" + new TranslatableComponent(items.get(0).getDescriptionId()).getString() + ")";
            }
        }
        return new TranslatableComponent(super.getTranslationKey(stack)).getString() + extra;
    }

    @Override
    public Component getDisplayName(FluidStack stack) {
        String extra = "";
        if (stack.hasTag() && stack.getTag().contains(NBT_TAG)){
            String tag = stack.getTag().getString(NBT_TAG);
            List<Item> items = SerializationTags.getInstance().getItems().getTagOrEmpty(new ResourceLocation(tag)).getValues();
            if (items.size() > 0){
                extra = " (" + new TranslatableComponent(items.get(0).getDescriptionId()).getString() + ")";
            }
        }
        return new TextComponent(super.getDisplayName(stack).getString() + extra);
    }

    public static FluidStack getFluidWithTag(OreFluidInstance fluidInstance, int amount, ResourceLocation itemITag){
        FluidStack stack = new FluidStack(fluidInstance.getSourceFluid(), amount);
        stack.getOrCreateTag().putString(NBT_TAG, itemITag.toString());
        return stack;
    }

    public static String getFluidTag(FluidStack stack){
        return stack.getOrCreateTag().getString(NBT_TAG);
    }

    public static boolean isValid(ResourceLocation resourceLocation){
        return SerializationTags.getInstance().getItems().getAvailableTags().contains(new ResourceLocation("forge:dusts/" + resourceLocation.toString().replace("forge:ores/", ""))) && !SerializationTags.getInstance().getItems().getTag(resourceLocation).getValues().isEmpty();
    }

    public static ItemStack getOutputDust(FluidStack stack){
        String tag = getFluidTag(stack);
        return TagUtil.getItemWithPreference(SerializationTags.getInstance().getItems().getTag(new ResourceLocation(tag.replace("forge:ores/", "forge:dusts/"))));
    }

    @Override
    public ItemStack getBucket(FluidStack stack) {
        ItemStack bucket = super.getBucket(stack);
        if(stack.hasTag() && stack.getTag().contains(NBT_TAG)) {
            String tag = stack.getTag().getString(NBT_TAG);
            bucket.getOrCreateTag().putString(NBT_TAG, tag);
        }
        return bucket;
    }
}
