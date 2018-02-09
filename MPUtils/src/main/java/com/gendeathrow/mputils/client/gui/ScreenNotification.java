package com.gendeathrow.mputils.client.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.mputils.utils.RenderAssist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ScreenNotification 
{
	
	static ArrayList<QuestNotice> notices = new ArrayList<QuestNotice>();

	
	public static void ScheduleNotice(List<String> lines, String sound)
	{
		notices.add(new QuestNotice(lines, sound));
	}
	
	@SubscribeEvent
	public static void onDrawScreen(RenderGameOverlayEvent.Post event)
	{
		if(event.getType() != RenderGameOverlayEvent.ElementType.HELMET || notices.size() <= 0)
		{
			return;
		}
		
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution resolution = new ScaledResolution(mc);
		int width = resolution.getScaledWidth();
		int height = resolution.getScaledHeight();
		QuestNotice notice = notices.get(0);
		
		if(!notice.init)
		{
			if(mc.isGamePaused() || mc.currentScreen != null)
			{
				return; // Do not start showing a new notice if the player isn't looking
			}
			
			notice.init = true;
			notice.startTime = Minecraft.getSystemTime();
			notice.lengthSecs = 6 + (notice.lines.size() * 0.5f);
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(new SoundEvent(new ResourceLocation(notice.sound)), 1.0F));
		}
		
		if(notice.getTime() >= notice.lengthSecs)
		{
			notices.remove(0);
			return;
		}
		
		GlStateManager.pushMatrix();
		
		float scale = width > 600? 1.5F : 1F;
		
		GlStateManager.scale(scale, scale, scale);
		width = MathHelper.ceil(width/scale);
		height = MathHelper.ceil(height/scale);
		
		float alpha = notice.getTime() <= notice.lengthSecs - 2? Math.min(1F, notice.getTime()) : Math.max(0F, notice.lengthSecs - 1 - notice.getTime());
		alpha = MathHelper.clamp(alpha, 0.02F, 1F);
		int color = new Color(1F, 1F, 1F, alpha).getRGB();
		Color bg = new Color(1/Color.DARK_GRAY.getRed(), 1/Color.DARK_GRAY.getGreen(), 1/Color.DARK_GRAY.getBlue(), alpha/2);
		Color bg2 = new Color(0, 0, 0, alpha);
		
		GlStateManager.color(1F, 1F, 1F, alpha);
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
     	
		String tmp = I18n.format(notice.getLine(0));
		int txtW = mc.fontRenderer.getStringWidth(tmp);
		
		
		float scaleing = 1.5f;
		float posX = (float)width/2 - (txtW * scaleing)/2;
		float posY = (float)height/4 - 8; 
		
		GuiScreen.drawRect(0, (int)posY - 4, width, height/4 + (12 * notice.lines.size()), bg.getRGB());

		RenderAssist.drawUnfilledRect(-5, (int)posY - 4, width+5, height/4 + (12 * notice.lines.size()), bg2.getRGB());
		GlStateManager.pushMatrix();

			GlStateManager.scale(scaleing, scaleing, 0);
			GlStateManager.translate(0, 0, 0);

			mc.fontRenderer.drawString(tmp, ((float)posX) / scaleing, ((float)posY) / scaleing, color, true);
		GlStateManager.popMatrix();
		for(int i = 1; i < notice.lines.size(); i++) {
			tmp = I18n.format(notice.getLine(i));
			txtW = mc.fontRenderer.getStringWidth(tmp);
			mc.fontRenderer.drawString(tmp, width/2 - txtW/2, height/4 + (12 * i), color, true);
		}
		
		//GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
	
	public static class QuestNotice
	{
		long startTime = 0;
		public float lengthSecs = 6f;
		public boolean init = false;
		public List<String> lines = new ArrayList<String>();
		public ItemStack icon = null;
		public String sound = "random.levelup";
		
		public QuestNotice(List<String> lines2, String sound)
		{
			this.startTime = Minecraft.getSystemTime();
			lines = lines2;
			this.sound = sound;
		}
		
		public String getLine(int line) {
			if(validate(line))
				return lines.get(line);
			return "";
		}
		
		private boolean validate(int line) {
			if(line < lines.size())
				return true;
			return false;
		}
		
		public float getTime()
		{
			return (Minecraft.getSystemTime() - startTime)/1000F;
		}
	}
}