#Importamos la libreria
from constraint import*

#Definicion del problema como variable
problem = Problem()

#Creacion de las variables (Primer caracter: 1 de manana, 2 de tarde)
problem.addVariable("SAT1",["11", "12", "13", "14"])
problem.addVariable("SAT2",["11", "12", "13"])
problem.addVariable("SAT31",["14", "16"])
problem.addVariable("SAT32",["27", "29", "210"])
problem.addVariable("SAT4",["28", "211", "212"])
problem.addVariable("SAT5",["11", "17", "112"])
problem.addVariable("SAT61",["17", "19"])
problem.addVariable("SAT62",["23", "24", "25"])

#Adicion de restricciones

#1, satelites 1  y 2 deben tener la misma antena asignada

problem.addConstraint(AllEqualConstraint(), ("SAT1", "SAT2")) #Usamos la funcion AllEqualConstraint, definida por defecto en la libreria 


#2, satelites 2,4  y 5 deben tener distintas antenas asignadas (Suponemos que independientemente de la franja)

def notEqual(a,b,c):
    return a[1:]!=b[1:] and a[1:]!=c[1:] and b[1:]!=c[1:]

problem.addConstraint(notEqual, ("SAT2", "SAT4", "SAT5")) #Variables en las que hay conflicto


#3, Si SAT5->ANT12, SAT4!->ANT11

def cannotCommunicate(a,b):
    if((a[1:]=="12" and b[1:]=="11")): #La unica condicion en la que habra conflicto es si cuando a=12, b=11 
        return False
    else:
        return True

problem.addConstraint(cannotCommunicate, ("SAT5", "SAT4"))


#4, ANT7 and ANT12 de manana y de tarde

def bothAerials(a,b):
    if(((a[1:]=="7" and b[1:]=="12") or (a[1:]=="12" and b[1:]=="7"))and(a[0:1]!=b[0:1])): #El conflicto sucede si a=7 y b=12 o a=12 y b=7, si la franja es distinta no se cumplira la restriccion 
        return False                                                                       #En el resto de casos habra validez
    else:
        return True


problem.addConstraint(bothAerials, ("SAT32", "SAT4"))
problem.addConstraint(bothAerials, ("SAT32", "SAT5"))
problem.addConstraint(bothAerials, ("SAT61", "SAT5"))
problem.addConstraint(bothAerials, ("SAT61", "SAT4"))
problem.addConstraint(bothAerials, ("SAT4", "SAT5"))

#Imprimimos la solucion
print(len(problem.getSolutions()))







