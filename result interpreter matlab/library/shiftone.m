function sallbmatrix = shiftone(allbmatrix, mrow, mcol, shiftnum)
%shifts a single bmatrix in the sallbmatrix
%sallbmatrix  :  output cell with all bmatrices
%allbmatrix   :  input cell with all bmatrices
%matrixnum    :  [row,col] which matrix in the sallbmatrix do you want to shift
%shiftnum     :  amount that you want to shift the matrix

sallbmatrix = allbmatrix;
sallbmatrix{mrow, mcol} = allbmatrix{mrow, mcol} + shiftnum;