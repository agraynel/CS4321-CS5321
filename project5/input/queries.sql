SELECT * 
FROM Sailors, Reserves
WHERE Reserves.H < 50 AND Sailors.A = Reserves.G
ORDER BY Reserves.G;

SELECT Sailors.A 
FROM Sailors
ORDER BY Sailors.A ;

SELECT DISTINCT Sailors.A 
FROM Sailors
ORDER BY Sailors.A ;

SELECT * 
FROM Sailors 
WHERE Sailors.B >= Sailors.C AND Sailors.A < 40
ORDER BY Sailors.A;

SELECT Reserves.H, Boats.E 
FROM Sailors, Reserves, Boats 
WHERE Sailors.A = Reserves.G AND Reserves.H = Boats.D AND Sailors.B >= 150 
ORDER BY Boats.E;

SELECT S.C,R.H,B.E
FROM Sailors S, Reserves R, Boats B 
WHERE S.A = R.G AND R.H = B.D AND B.E > 170 AND S.B < 60
ORDER BY S.C;

SELECT B.F, S.A, S.C
FROM Sailors S, Reserves R, Boats B
WHERE S.A = R.G AND R.H = B.D AND B.D <= 66 AND S.B > 150
ORDER BY S.C;

SELECT DISTINCT R.G, S.B, B.F
FROM Boats B, Reserves R, Sailors S
WHERE S.A = R.G AND R.H = B.D AND B.D !=1 AND S.B > 30 AND R.G > 177
ORDER BY S.B;

SELECT DISTINCT S1.A, R1.H, S1.B, B.D
FROM Reserves R1, Sailors S1, Boats B
WHERE B.D >= 55 AND B.D < 132 AND S1.A = R1.H AND S1.A < 77 AND S1.A >= 35 
ORDER BY B.D, R1.H;

SELECT DISTINCT R1.G, R2.G
FROM Reserves R1, Reserves R2 
WHERE R1.G < R2.G AND R1.H > 130
ORDER BY R2.G;

SELECT *
FROM Reserves R1, Reserves R2
WHERE R1.H = R2.H
ORDER BY R2.H;