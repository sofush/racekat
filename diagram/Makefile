.PHONY: clean all
PLANTUML=plantuml

all: domain-model.png class-diagram.png entity-relationship.png

domain-model.png: domain-model.txt
	$(PLANTUML) domain-model.txt

class-diagram.png: class-diagram.txt
	$(PLANTUML) class-diagram.txt

entity-relationship.png: entity-relationship.txt
	$(PLANTUML) entity-relationship.txt

clean:
	rm *.png

