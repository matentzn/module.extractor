# Module extractor

Example command:
D:\cto.clinical-trials-ontology.1.owl.xml bottom D:\seeds.txt D:\cto.module.owl

Example seeds.txt:

-------------seeds.txt start-------------
# use cl| for classes, op| for object properties, dp| for data properties and ni| for individuals
cl|http://www.semanticweb.org/ontologies/2012/0/Ontology1325521724189.owl#Scale_for_Depression
http://www.co-ode.org/ontologies/ont.owl#Historical_observational_cohort
http://www.semanticweb.org/ontologies/2012/0/Ontology1325521724189.owl#Study_Objective
-------------seeds.txt end-------------

Options:
[1] Path to ontology
[2] Module Type (bottom, top, star), all syntactic locality.
[3] Path to file with seeds. Note that you need to indicate the type (see example file below).
[4] Output file. Parent directory needs to exist