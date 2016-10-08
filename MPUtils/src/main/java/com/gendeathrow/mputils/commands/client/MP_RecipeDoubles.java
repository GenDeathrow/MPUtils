package com.gendeathrow.mputils.commands.client;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.utils.FakeInventoryCrafting;
import com.gendeathrow.mputils.utils.Tools;

public class MP_RecipeDoubles extends MP_BaseCommand
{
	
	ArrayList<ShapedRecipes> shapedList = new ArrayList<ShapedRecipes>();
	ArrayList<ShapedOreRecipe> shapedOreList = new ArrayList<ShapedOreRecipe>();
	
	
	ArrayList<ShapelessRecipes> shapelessList = new ArrayList<ShapelessRecipes>();
	ArrayList<ShapelessOreRecipe> shapelessOreList = new ArrayList<ShapelessOreRecipe>();
	
	HashMap<ShapedRecipes, ArrayList> foundShapedList = new HashMap<ShapedRecipes, ArrayList>();
	
	HashMap<ShapelessRecipes, ArrayList> foundShapelessList = new HashMap<ShapelessRecipes, ArrayList>();
	
	HashMap<ShapelessOreRecipe, ArrayList> foundShapelessOreList = new HashMap<ShapelessOreRecipe, ArrayList>();
	
	HashMap<ShapedOreRecipe, ArrayList> foundShapedOreList = new HashMap<ShapedOreRecipe, ArrayList>();
	

	@Override
	public String getCommand() 
	{
		return "recipeDuplicates";
	}
	
	String fileData;

	@Override
	public void runCommand(CommandBase command, ICommandSender sender, String[] args) 
	{
		
		fileData = "";
		addLine("######################################################################################");
		addLine("#                                     MPUTILS                                        #");
		addLine("# -----------------------------------------------------------------------------------#");
		addLine("#  This tool is used to find Duplicate Recipes.                                      #");
		addLine("#  This is Beta Testing for finding Duplicate Recipes                                #");
		addLine("#  You will have to check recipes as you may get false positives or incorrect ids    #");
		addLine("#  This is mainly dealing with Ore Dictonary recipes.                                #");
		addLine("#  This tool will be will be perfected over time.                                    #");
		addLine("#  Also format is close to MineTweakers ,But you may have to tweak it to work        #");
		addLine("######################################################################################");
		addLine("");addLine("");addLine("");
		
		SortList();
		
		addLine("Serching for Duplicates Recipes");
		addLine("---------------------------------------");
		
		
		findShappedRecipes(sender);
		
		findShapedOreRecipes(sender);
		
		findShaplessRecipes(sender);
		
		findShaplessOreRecipes(sender);
		
		addLine("");addLine("");
		addLine("Found Recipes Duplicates:");
		addLine(">---------------------------------------");
		addLine(">	Shaped Ore Recipes Found: "+ foundShapedOreList.size());
		addLine(">	Shaped Recipes Found: "+ foundShapedList.size());
		addLine(">	Shapeless Recipes Found: "+ foundShapelessList.size());
		addLine(">	Shapeless Ore Recipes Found: "+ foundShapelessOreList.size() );
		addLine(">  Total: " + (foundShapedOreList.size() + foundShapedList.size() + foundShapelessList.size() + foundShapelessOreList.size()));
		addLine("---------------------------------------");
		
		String reply = Tools.CreateSaveFile(new File(Minecraft.getMinecraft().mcDataDir, "mputils/recipeDump.txt"), fileData);
		
		
		sender.addChatMessage(new TextComponentTranslation("Shaped Ore Recipes Found: "+ foundShapedOreList.size()));
		sender.addChatMessage(new TextComponentTranslation("Shaped Recipes Found: "+ foundShapedList.size()));
		sender.addChatMessage(new TextComponentTranslation("Shapeless Recipes Found: "+ foundShapelessList.size()));
		sender.addChatMessage(new TextComponentTranslation("Shapeless Ore Recipes Found: "+ foundShapelessOreList.size()));
		sender.addChatMessage(new TextComponentTranslation(reply));
		
		
	}
	
	/**
	 * Sort recipes into appropriate lists
	 */
	private void SortList()
	{
		this.shapedList.clear();
		this.shapelessList.clear();
		this.shapelessOreList.clear();
		this.shapedOreList.clear();
		
		this.foundShapedList.clear();
		this.foundShapelessList.clear();
		this.foundShapelessOreList.clear();
		this.foundShapedOreList.clear();
		
		List<IRecipe> recipeList = CraftingManager.getInstance().getRecipeList();
		
		for(int i=0; i < recipeList.size(); i++)
		{
			IRecipe recipe= recipeList.get(i);
			
			if(recipe instanceof ShapedRecipes)
			{
				shapedList.add((ShapedRecipes)recipe);
			}
			else if(recipe instanceof ShapelessRecipes)
			{
				shapelessList.add((ShapelessRecipes)recipe);
			}
			else if(recipe instanceof ShapedOreRecipe)
			{
				shapedOreList.add((ShapedOreRecipe) recipe);
			}
			else if (recipe instanceof ShapelessOreRecipe)
			{
				shapelessOreList.add((ShapelessOreRecipe)recipe);
			}

			
		}
		
		addLine("");addLine("");
		addLine("Total Recipes Found:");
		addLine(">---------------------------------------");
		addLine(">   Shaped Recipes Found: "+ shapedList.size()); 
		addLine(">   Shaped Ore Recipes Found: "+ shapedOreList.size());
		addLine(">   Shapeless Recipes Found: "+ shapelessList.size());
		addLine(">   Shapeless Ore Recipes Found: "+ this.shapedOreList.size());
		addLine(">   Total: "+ recipeList.size());
		addLine(">---------------------------------------");
		
		addLine("");addLine("");addLine("");
	}
	
	private void findShappedRecipes(ICommandSender sender)
	{
		// SHAPED RECIPE -------------------------------------------
		addLine("");
		addLine("  <Shaped Recipes>");
		
		for(ShapedRecipes recipe : shapedList)
		{
			
			ArrayList<ShapedRecipes> matches = new ArrayList<ShapedRecipes>();
			
			int k = recipe.recipeHeight;
			int j = recipe.recipeWidth;
			
			FakeInventoryCrafting fakeCrafter = new FakeInventoryCrafting(j > 3 ? recipe.recipeWidth : 3,k > 3 ? recipe.recipeHeight : 3);
			
			for(int row = 0; row < fakeCrafter.inventoryHeight; row++)
			{
				for(int column = 0; column < fakeCrafter.inventoryWidth; column++)
				{
		        	ItemStack target = null;
		        	
						if(row < recipe.recipeHeight && column < recipe.recipeWidth)
						{
							ItemStack stack = recipe.recipeItems[column + row * recipe.recipeWidth];
							fakeCrafter.setStackInRowAndColumn(row, column, stack);
						}
				}
			}
			
			for(ShapedRecipes compareTo : shapedList)
			{
				
				if(recipe.equals(compareTo)) continue;
				
				
				if(foundShapedList.containsKey(compareTo)) 
				{
					Iterator checkifMatched = foundShapedList.get(compareTo).iterator();
					boolean flag = false;
					while (checkifMatched.hasNext())
					{
						if(checkifMatched.next().equals(recipe))
						{
							flag = true;
						}
							
					}
					
					if(flag) continue;
				}
				
				// Ignore recipes from same mod.. as they should haven't overwritten their own mods recipes.
				if(getModID(recipe.getRecipeOutput().getItem()) ==  getModID(compareTo.getRecipeOutput().getItem()))
				{
					continue;
				}

				if(recipe.getRecipeOutput().getItem() == compareTo.getRecipeOutput().getItem() && recipe.getRecipeOutput().getItemDamage() == compareTo.getRecipeOutput().getItemDamage())
				{
					continue;
				}
				else if(compareTo.recipeWidth != recipe.recipeWidth && compareTo.recipeHeight != recipe.recipeHeight)
				{
					continue;
				}
				
				if(compareTo.matches(fakeCrafter, sender.getEntityWorld()) )
				{
					printMatch(recipe.getRecipeOutput(), compareTo.getRecipeOutput(), fakeCrafter);
					matches.add(compareTo);
				}
			}
			
			if(matches.size() > 0)
			{
				this.foundShapedList.put(recipe, matches);
			}
		}
		
		addLine("  </Shaped Recipes>");
	// --------------------------------------------------------	
	}
	
	private void findShapedOreRecipes(ICommandSender sender)
	{
		
		Field privateHeightField = null;
		Field privateWidthField = null;
		try 
		{
			privateHeightField = ShapedOreRecipe.class.getDeclaredField("height");
			privateWidthField = ShapedOreRecipe.class.getDeclaredField("width");

			privateHeightField.setAccessible(true);
			privateWidthField.setAccessible(true);

		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}

		
	// SHAPED ORE -------------------------
		
		addLine("");
		addLine("  <Shaped Ore Dictonary>");
		for (ShapedOreRecipe recipe : shapedOreList)
		{

			ArrayList<ShapedOreRecipe> matches = new ArrayList<ShapedOreRecipe>();
			
			
			
			int recipeHeight = 3;
			int recipeWidth = 3;
			
			try 
			{
				recipeWidth = (Integer) privateWidthField.get(recipe);
				recipeHeight = (Integer) privateHeightField.get(recipe);
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			}
			
			
			int k = recipeHeight;
			int j = recipeWidth;
			
			FakeInventoryCrafting fakeCrafter = new FakeInventoryCrafting(j > 3 ? recipeWidth : 3,k > 3 ? recipeHeight : 3);

			for(int row = 0; row < fakeCrafter.inventoryHeight; row++)
			{
				for(int column = 0; column < fakeCrafter.inventoryWidth; column++)
				{
		        	ItemStack target = null;
		        	
						if(row < recipeHeight && column < recipeWidth)
						{

							Object inputItem = null;
							
							inputItem = recipe.getInput()[column + row * recipeWidth];
	
			                if (inputItem instanceof ItemStack)
			                {
			                	target = (ItemStack) inputItem;
			                }
			                else if (inputItem instanceof ArrayList)
			                {
			                    Iterator<ItemStack> itr = ((ArrayList<ItemStack>)inputItem).iterator();
			                    
			                    if(itr.hasNext()) target = (ItemStack)itr.next();
		
			                }
			  
							fakeCrafter.setStackInRowAndColumn(row, column, target);
						}
						
						
				}
				
			}
			
			for(ShapedOreRecipe compareTo : shapedOreList)
			{
				if(recipe.equals(compareTo)) continue;
				
				
				if(foundShapedOreList.containsKey(compareTo)) 
				{
					Iterator checkifMatched = foundShapedOreList.get(compareTo).iterator();
					boolean flag = false;
					while (checkifMatched.hasNext())
					{
						if(checkifMatched.next().equals(recipe))
						{
							flag = true;
						}
							
					}
					
					if(flag) continue;
				}
				
				int comRecipeHeight = 3;
				int comRecipewidth = 3;
				
				try 
				{
					comRecipewidth = (Integer) privateWidthField.get(compareTo);
					comRecipeHeight = (Integer) privateHeightField.get(compareTo);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) 
				{
					e.printStackTrace();
				}

				if(getModID(recipe.getRecipeOutput().getItem()) ==  getModID(compareTo.getRecipeOutput().getItem()))
				{
					continue;
				}

				if(recipe.getRecipeOutput().getItem() == compareTo.getRecipeOutput().getItem() && recipe.getRecipeOutput().getItemDamage() == compareTo.getRecipeOutput().getItemDamage())
				{
					continue;
				}
				else if(comRecipewidth != recipeWidth && comRecipeHeight != recipeHeight)
				{
					continue;
				}
				
				if(compareTo.matches(fakeCrafter, sender.getEntityWorld()) )
				{
					printMatch(recipe.getRecipeOutput(), compareTo.getRecipeOutput(), fakeCrafter);
					matches.add(compareTo);			
				}
			}
			
			if(matches.size() > 0)
			{
				this.foundShapedOreList.put(recipe, matches);
			}
			
		}
		
		addLine("  </Shaped Ore Dictonary>");
	}
	
	
	private String getModID(Item item)
	{
		return item.getRegistryName().toString().split(":")[0];
	}
	
	private void findShaplessRecipes(ICommandSender sender)
	{
		addLine("");
		addLine("  <Shapeless Recipes>");

		for(ShapelessRecipes recipe : shapelessList)
		{
			ArrayList<ShapelessRecipes> matches = new ArrayList<ShapelessRecipes>();
			
			Iterator<ItemStack> itemstacks = recipe.recipeItems.iterator();
			
			FakeInventoryCrafting fakeCrafter = new FakeInventoryCrafting(3,3);
				
			for(int row = 0; row < fakeCrafter.inventoryHeight; row++)
			{
				for(int column = 0; column < fakeCrafter.inventoryWidth; column++)
				{
					ItemStack stack = null; 
					if(itemstacks.hasNext()) stack = itemstacks.next();

					fakeCrafter.setStackInRowAndColumn(row, column, stack);
						
				}
					
			}
			
			for(ShapelessRecipes compareTo : shapelessList)
			{
				
				if(recipe.equals(compareTo)) continue;
				
				if(foundShapelessList.containsKey(compareTo)) 
				{
					Iterator checkifMatched = foundShapelessList.get(compareTo).iterator();
					boolean flag = false;
					while (checkifMatched.hasNext())
					{
						if(checkifMatched.next().equals(recipe))
						{
							flag = true;
						}
							
					}
					
					if(flag) continue;
				}
				
				if(getModID(recipe.getRecipeOutput().getItem()) ==  getModID(compareTo.getRecipeOutput().getItem()))
				{
					continue;
				}
				if(recipe.getRecipeOutput().getItem() == compareTo.getRecipeOutput().getItem() && recipe.getRecipeOutput().getItemDamage() == compareTo.getRecipeOutput().getItemDamage())
				{
					continue;
				}
				
				boolean flag = false;
				

				if(compareTo.matches(fakeCrafter, sender.getEntityWorld()))
				{
					printMatch(recipe.getRecipeOutput(), compareTo.getRecipeOutput(), fakeCrafter);
					
					matches.add(compareTo);
				}
				
			}
			
			if(matches.size() > 0)
			{
				this.foundShapelessList.put(recipe, matches);
			}
		}
		
		addLine("  </Shapeless Recipes>");
	}
	
	
	private void findShaplessOreRecipes(ICommandSender sender)
	{
		addLine("");
		addLine("  <Shapeless Ore Recipes>");
		
		
		

		for(ShapelessOreRecipe recipe : shapelessOreList)
		{
			ArrayList<ShapelessOreRecipe> matches = new ArrayList<ShapelessOreRecipe>();
			
			
			Iterator<Object> itemstacks = recipe.getInput().iterator();
			
			FakeInventoryCrafting fakeCrafter = new FakeInventoryCrafting(3,3);
				
			for(int row = 0; row < fakeCrafter.inventoryHeight; row++)
			{
				for(int column = 0; column < fakeCrafter.inventoryWidth; column++)
				{
					ItemStack stack = null; 
					if(itemstacks.hasNext()) 
					{
						Object inputItem = itemstacks.next();
						
		                if (inputItem instanceof ItemStack)
		                {
		                	stack = (ItemStack) inputItem;
		                }
		                else if (inputItem instanceof ArrayList)
		                {
		                    Iterator<ItemStack> itr = ((ArrayList<ItemStack>)inputItem).iterator();
		                    
		                    if(itr.hasNext()) stack = (ItemStack)itr.next();
	
		                }
						
					}

					fakeCrafter.setStackInRowAndColumn(row, column, stack);
						
				}
					
			}

			
			for(ShapelessOreRecipe compareTo : shapelessOreList)
			{
				
				if(recipe.equals(compareTo)) continue;

				if(foundShapelessOreList.containsKey(compareTo)) 
				{
					Iterator checkifMatched = foundShapelessOreList.get(compareTo).iterator();
					boolean flag = false;
					while (checkifMatched.hasNext())
					{
						if(checkifMatched.next().equals(recipe))
						{
							flag = true;
						}
							
					}
					
					if(flag) continue;
				}
				
				if(getModID(recipe.getRecipeOutput().getItem()) ==  getModID(compareTo.getRecipeOutput().getItem()))
				{
					continue;
				}
				if(recipe.getRecipeOutput().getItem() == compareTo.getRecipeOutput().getItem() && recipe.getRecipeOutput().getItemDamage() == compareTo.getRecipeOutput().getItemDamage())
				{
					continue;
				}
				
				boolean flag = false;
				

				if(compareTo.matches(fakeCrafter, sender.getEntityWorld()))
				{
					printMatch(recipe.getRecipeOutput(), compareTo.getRecipeOutput(), fakeCrafter);
					
					matches.add(compareTo);
				}
				
			}
			
			if(matches.size() > 0)
			{
				this.foundShapelessOreList.put(recipe, matches);
			}
		}
		
		addLine("  </Shapeless Ore Recipes>");
	}
	
	private void addLine(String newLine)
	{
		fileData += newLine + System.getProperty("line.separator");
	}
	
	
	private void printMatch(ItemStack stack1, ItemStack stack2, FakeInventoryCrafting crafter)
	{
		addLine("	*************************************************************************************");
		addLine("	*  Found:"+ stack1.getDisplayName() +" And "+  stack2.getDisplayName());
		addLine("	*    Output Ids: <"+ stack1.getItem().getRegistryName() +":"+ stack1.getMetadata() +"> ("+ stack1.getDisplayName()+") && <"+  stack2.getItem().getRegistryName() +":"+ stack2.getMetadata() +">("+ stack2.getDisplayName()+")");
		addLine("	*    Recipe: "+crafter.printOut());
		addLine("	*************************************************************************************");
		addLine("");
	}
	

}
