SELECT * 
FROM Sailors;

SELECT Sailors.A 
FROM Sailors;

SELECT S.A 
FROM Sailors S;

SELECT * 
FROM Sailors S 
WHERE S.A < 3;

SELECT * 
FROM Sailors, Reserves 
WHERE Sailors.A = Reserves.G;

SELECT * 
FROM Sailors S1, Sailors S2 
WHERE S1.A < S2.A;

SELECT DISTINCT R.G 
FROM Reserves R;

SELECT R.G 
FROM Reserves R;

SELECT * 
FROM Sailors 
ORDER BY Sailors.B;

SELECT *
FROM Sailors S, Reserves R, Boats B
WHERE S.A = R.G AND R.H = B.D AND B.D <= 103
ORDER BY S.C, B.E;

SELECT B.F, S.A, S.B, S.C
FROM Sailors S, Reserves R, Boats B
WHERE S.A = R.G AND R.H = B.D AND B.D <= 103
ORDER BY S.C, B.F;

SELECT B.F, S.A, S.B, S.C
FROM Sailors S, Reserves R, Boats B
WHERE S.A = R.G AND R.H = B.D AND B.D <= 103
ORDER BY S.C;

SELECT *
FROM Reserves R, Boats B, Sailors S
WHERE S.A = R.G AND R.H = B.D AND B.D <= 103
ORDER BY S.C;

SELECT DISTINCT S.A, S.B, S.C
FROM Boats B, Reserves R, Sailors S
WHERE S.A = R.G AND R.H = B.D AND B.D != 103
ORDER BY S.B;

SELECT DISTINCT R1.G, R2.G
FROM Reserves R1, Reserves R2 
WHERE R1.G < R2.G;

SELECT *
FROM Reserves R1, Reserves R2
ORDER BY R2.H

SELECT DISTINCT R1.G, R2.H, R3.G
FROM Reserves R1, Reserves R2, Reserves R3
WHERE R1.G<=2 AND R2.H > 102 ;

SELECT DISTINCT S1.A, R1.H, S1.B, S2.A, R2.H, B.D
FROM Reserves R1, Reserves R2, Sailors S1, Sailors S2 , Boats B
WHERE B.D >= 102 AND B.D < 104 AND S1.A = R1.H AND S1.A != S2.A AND S2.A = R2.H AND S2.A < 6 AND S2.A >= 3 AND S1.A <=3
ORDER BY B.D, S2.A;