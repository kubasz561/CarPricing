param N;
param M;
param C {1..M};
param c {1..M, 1..N};
var w {j in 1..N} >= 0, <= 1;

minimize f_celu: sum {i in 1..M} (C[i] - sum{j in 1..N}w[j]*c[i,j])^2;

subject to ogr1: sum{j in 1..N} w[j] = 1;
