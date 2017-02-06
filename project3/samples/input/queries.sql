SELECT * FROM Sailors;
SELECT Sailors.A FROM Sailors;
SELECT Boats.F, Boats.D FROM Boats;
SELECT Reserves.G, Reserves.H FROM Reserves;
SELECT * FROM Sailors WHERE Sailors.B >= Sailors.C;
SELECT Sailors.A FROM Sailors WHERE Sailors.B >= Sailors.C;
SELECT Sailors.A FROM Sailors WHERE Sailors.B >= Sailors.C AND Sailors.B < Sailors.C ORDER BY Sailors.A;

SELECT * FROM Sailors, Reserves WHERE Sailors.A = Reserves.G ORDER BY Reserves.G;
SELECT * FROM Sailors, Reserves, Boats WHERE Sailors.A = Reserves.G AND Reserves.H = Boats.D ORDER BY Boats.D, Reserves.G;
SELECT * FROM Sailors, Reserves, Boats WHERE Sailors.A = Reserves.G AND Reserves.H = Boats.D AND Sailors.B < 150 ORDER BY Boats.D;

SELECT DISTINCT * FROM Sailors;

SELECT * FROM Sailors S1, Sailors S2 WHERE S1.A < S2.A ORDER BY S2.A;

SELECT B.F, B.D FROM Boats B ORDER BY B.D;
SELECT * FROM Sailors S, Reserves R, Boats B WHERE S.A = R.G AND R.H = B.D ORDER BY S.C;
SELECT DISTINCT * FROM Sailors S, Reserves R, Boats B WHERE S.A = R.G AND R.H = B.D ORDER BY S.C;
SELECT B.F, S.A, S.B, S.C
FROM Sailors S, Reserves R, Boats B
WHERE S.A = R.G AND R.H = B.D AND B.D <= 66 AND S.B > 150
ORDER BY S.C;

SELECT *
FROM Reserves R, Boats B, Sailors S
WHERE S.A = R.G AND R.H = B.D AND B.D > 130
ORDER BY S.C;

SELECT DISTINCT S.A, S.B, S.C
FROM Boats B, Reserves R, Sailors S
WHERE S.A = R.G AND R.H = B.D AND B.D !=1
ORDER BY S.B;

SELECT DISTINCT S1.A, R1.H, S1.B, B.D
FROM Reserves R1, Sailors S1, Boats B
WHERE B.D >= 55 AND B.D < 132 AND S1.A = R1.H AND S1.A < 77 AND S1.A >= 35 
ORDER BY B.D, R1.H;

SELECT DISTINCT R1.G, R2.G
FROM Reserves R1, Reserves R2 
WHERE R1.G < R2.G
ORDER BY R2.G;

SELECT *
FROM Reserves R1, Reserves R2
WHERE R1.H = R2.H
ORDER BY R2.H;