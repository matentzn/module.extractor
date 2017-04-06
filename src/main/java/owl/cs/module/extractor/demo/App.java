package owl.cs.module.extractor.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import uk.ac.manchester.cs.owlapi.modularity.ModuleType;
import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		File ontologyf = new File(args[0]);
		String s_moduletype = args[1];
		File seedf = new File(args[2]);
		File outf = new File(args[3]);

		if (!seedf.exists()) {
			System.out.println("Seed file does not exist!");
			return;
		}

		if (!ontologyf.exists()) {
			System.out.println("Ontology file does not exist!");
			return;
		}

		if (!outf.getParentFile().exists()) {
			System.out.println("Target output file has to be placed in an existing directory!");
			return;
		}

		ModuleType moduleType = null;
		switch (s_moduletype) {
		case "bottom":
			moduleType = ModuleType.BOT;
			break;
		case "top":
			moduleType = ModuleType.TOP;
			break;
		case "star":
			moduleType = ModuleType.STAR;
			break;
		default:
			moduleType = ModuleType.BOT;
			System.out.println("Illegal module type parameter, taking bottom: " + s_moduletype);
		}

		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		
		OWLOntology o;
		try {
			Set<OWLEntity> seeds = preprocessSeed(seedf, man);
			o = man.loadOntologyFromOntologyDocument(ontologyf);
			SyntacticLocalityModuleExtractor ex = new SyntacticLocalityModuleExtractor(man, o, moduleType);
			Set<OWLAxiom> ax = ex.extract(seeds);
			System.out.println("Ontology size: "+o.getLogicalAxiomCount());
			System.out.println("Seeds: "+seeds.size());
			System.out.println("Axioms: "+ax.size());
			OWLOntology mod = OWLManager.createOWLOntologyManager().createOntology(ax);
			OutputStream os = new FileOutputStream(outf);
			mod.getOWLOntologyManager().saveOntology(mod,os);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Set<OWLEntity> preprocessSeed(File seedf, OWLOntologyManager man) throws IOException {
		OWLDataFactory df = man.getOWLDataFactory();
		List<String> seedlist = FileUtils.readLines(seedf, StandardCharsets.UTF_8);
		Set<OWLEntity> seeds = new HashSet<>();
		for (String seed : seedlist) {
			if(seed.startsWith("#")) {
				//comment in seed file.
				continue;
			}
			System.out.println("Processing seed: "+seed);
			if (seed.contains("|")) {
				String et_s = seed.split("|")[0];
				EntityType et = determineEntityType(et_s);
				IRI iri = IRI.create(seed.split("|")[1]);
				seeds.add(df.getOWLEntity(et, iri));
			} else {
				IRI iri = IRI.create(seed);
				seeds.add(df.getOWLEntity(EntityType.CLASS, iri));
			}
		}
		return seeds;
	}

	private static EntityType determineEntityType(String et_s) {
		EntityType et;
		switch (et_s) {
		case "cl":
			et = EntityType.CLASS;
			break;
		case "op":
			et = EntityType.OBJECT_PROPERTY;
			break;
		case "dp":
			et = EntityType.DATA_PROPERTY;
			break;
		case "ni":
			et = EntityType.NAMED_INDIVIDUAL;
			break;
		default:
			et = EntityType.CLASS;
		}
		return et;
	}
}
