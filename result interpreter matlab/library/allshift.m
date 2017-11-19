function sallbmatrix = allshift(allbmatrix, shiftamount)
%shift all matrices in the cell
%sallbmatrix  :  output cell with all bmatrices shifted by shiftamount
%allbmatrix   :  input cell with origional bmatrices
%shiftamount  :  amount of shift (V) desired

%define sallbmatrix size
sallbmatrix = cell(size(allbmatrix,1), size(allbmatrix,2));

%iterate through all bmatrix and add shift amount
for j = 1 : size(allbmatrix,2)
    for i = 1 : size(allbmatrix,1)
        sallbmatrix{i,j} = allbmatrix{i,j} + shiftamount;
    end
end

