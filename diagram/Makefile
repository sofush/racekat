.PHONY: clean all
PLANTUML=plantuml

all: domain-model.png class-diagram.png entity-relationship.png system-sequence-diagram.png sequence-diagram.png

domain-model.png: domain-model.txt
	$(PLANTUML) domain-model.txt

class-diagram.png: class-diagram.txt
	$(PLANTUML) class-diagram.txt

entity-relationship.png: entity-relationship.txt
	$(PLANTUML) entity-relationship.txt

system-sequence-diagram.png: system-sequence-diagram.txt
	$(PLANTUML) system-sequence-diagram.txt

sequence-diagram.png: sequence-diagram.txt
	$(PLANTUML) sequence-diagram.txt

clean:
	rm *.png

