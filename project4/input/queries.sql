SELECT DISTINCT A.B 
FROM Sailors A, Sailors B 
WHERE A.A = B.A AND B.B > 7800
ORDER BY A.B;

SELECT Boats.E, Sailors.C
FROM Sailors, Boats 
WHERE Sailors.B = Boats.F AND Sailors.A > 9000 AND Boats.E < 100 AND Boats.D != 1200 ORDER BY Sailors.C;

SELECT * FROM Sailors WHERE Sailors.B < 300 ORDER BY Sailors.B;

SELECT Boats.E, Sailors.C
FROM Sailors, Boats 
WHERE Boats.F <= 200 AND Boats.E <= Sailors.B AND Sailors.A > 9000 AND Sailors.B = Boats.F
ORDER BY Sailors.C;

SELECT DISTINCT S1.C, S2.A, B.D
FROM Sailors S1, Sailors S2, Boats B
WHERE B.F >= 3000 AND B.F < 5000 AND S1.A = B.D AND S1.A < 2000
ORDER BY B.D, S2.A;

SELECT DISTINCT B1.D, B2.F
FROM Boats B1, Boats B2
WHERE B1.E = B2.E AND B1.E < 1000 AND B1.D > B2.F AND B2.D >=8000 AND B1.E != 200
ORDER BY B2.F;

SELECT B2.E
FROM Boats B1, Boats B2, Boats B3
WHERE B1.D = B2.F AND B3.E = B2.F AND B1.E < 200 AND B2.D > B3.E AND B2.F >=9000 AND B1.F <> 5000 AND B3.D < 4100 AND B3.D > 3333;