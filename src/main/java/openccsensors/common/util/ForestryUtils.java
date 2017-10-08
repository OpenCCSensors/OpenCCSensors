package openccsensors.common.util;

import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.ITreeGenome;
import forestry.api.genetics.*;
import forestry.api.lepidopterology.IButterfly;
import forestry.api.lepidopterology.IButterflyGenome;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3i;

import java.util.HashMap;
import java.util.Map;

public class ForestryUtils {

	private static ISpeciesRoot beeRoot = AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");
	private static ISpeciesRoot treeRoot = AlleleManager.alleleRegistry.getSpeciesRoot("rootTrees");
	private static ISpeciesRoot butterflyRoot = AlleleManager.alleleRegistry.getSpeciesRoot("rootButterflies");

	public static Map<String, Object> genomeToMap(ItemStack item) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		if (beeRoot.isMember(item) && beeRoot.getMember(item).isAnalyzed()) {
			response.putAll(beeGenomeToMap(beeRoot.getMember(item)));
		} else if (treeRoot.isMember(item) && treeRoot.getMember(item).isAnalyzed()) {
			response.putAll(treeGenomeToMap(treeRoot.getMember(item)));
		} else if (butterflyRoot.isMember(item) && butterflyRoot.getMember(item).isAnalyzed()) {

		}
		return response;
	}

	private static Object getAlleleValue(IAllele allele) {
		if (allele instanceof IAlleleBoolean) {
			return ((IAlleleBoolean) allele).getValue();
		} else if (allele instanceof IAlleleInteger) {
			return ((IAlleleInteger) allele).getValue();
		} else if (allele instanceof IAlleleFloat) {
			return ((IAlleleFloat) allele).getValue();
		} else if (allele instanceof IAlleleTolerance) {
			return ((IAlleleTolerance) allele).getValue();
		} else if (allele instanceof IAlleleArea) {
			Vec3i area = ((IAlleleArea) allele).getValue();
			return area.getX() + 'x' + area.getY() + 'x' + area.getZ();
		} else if (allele instanceof IAlleleFlowers) {
			return ((IAlleleFlowers) allele).getProvider().getDescription();
		}
		return null;
	}

	public static Map<String, Object> beeGenomeToMap(IIndividual individual) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		if (individual instanceof IBee) {
			IBee bee = (IBee) individual;
			IBeeGenome genome = bee.getGenome();
			IChromosome[] chromosomes = genome.getChromosomes();
			HashMap<String, Object> primary = new HashMap<String, Object>();
			HashMap<String, Object> secondary = new HashMap<String, Object>();
			for (IChromosome chromosome : chromosomes) {
				IAllele priAllele = chromosome.getPrimaryAllele();
				IAllele secAllele = chromosome.getSecondaryAllele();
				primary.put(priAllele.getName(), getAlleleValue(priAllele));
				secondary.put(secAllele.getName(), getAlleleValue(secAllele));
			}
			primary.put("Species", genome.getPrimary().getUID());
			secondary.put("Species", genome.getSecondary().getUID());
			response.put("Primary", primary);
			response.put("Secondary", secondary);
			response.put("Speed", genome.getSpeed());
			response.put("Lifespan", genome.getLifespan());
			response.put("Fertility", genome.getFertility());
			response.put("TemperatureTolerance", genome.getToleranceTemp());
			response.put("HumidityTolerance", genome.getToleranceHumid());
			response.put("IsNocturnal", genome.getNeverSleeps());
			response.put("IsTolerantFlyer", genome.getToleratesRain());
			response.put("IsCaveDweller", genome.getCaveDwelling());
		}

		return response;
	}

	public static Map<String, Object> treeGenomeToMap(IIndividual individual) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		if (individual instanceof ITree) {
			ITree tree = (ITree) individual;
			ITreeGenome genome = tree.getGenome();
			IChromosome[] chromosomes = genome.getChromosomes();
			HashMap<String, Object> primary = new HashMap<String, Object>();
			HashMap<String, Object> secondary = new HashMap<String, Object>();
			for (IChromosome chromosome : chromosomes) {
				IAllele priAllele = chromosome.getPrimaryAllele();
				IAllele secAllele = chromosome.getSecondaryAllele();
				primary.put(priAllele.getName(), getAlleleValue(priAllele));
				secondary.put(secAllele.getName(), getAlleleValue(secAllele));
			}
			primary.put("Species", genome.getPrimary().getUID());
			secondary.put("Species", genome.getSecondary().getUID());
			response.put("Primary", primary);
			response.put("Secondary", secondary);
		}
		return response;
	}

	public static Map<String, Object> butterflyGenomeToMap(IIndividual individual) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		if (individual instanceof IButterfly) {
			IButterfly butterfly = (IButterfly) individual;
			IButterflyGenome genome = butterfly.getGenome();
			IChromosome[] chromosomes = genome.getChromosomes();
			HashMap<String, Object> primary = new HashMap<String, Object>();
			HashMap<String, Object> secondary = new HashMap<String, Object>();
			for (IChromosome chromosome : chromosomes) {
				IAllele priAllele = chromosome.getPrimaryAllele();
				IAllele secAllele = chromosome.getSecondaryAllele();
				primary.put(priAllele.getName(), getAlleleValue(priAllele));
				secondary.put(secAllele.getName(), getAlleleValue(secAllele));
			}
			primary.put("Species", genome.getPrimary().getUID());
			secondary.put("Species", genome.getSecondary().getUID());
			response.put("Primary", primary);
			response.put("Secondary", secondary);
		}
		return response;
	}
}
