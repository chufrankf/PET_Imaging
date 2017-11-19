function bshiftauto = shiftavg(oldbmat)
%bshiftauto = shiftavg(oldbmat, avgmatrix)
%Generates shift to 0.
%bshiftauto  :  output allbmatrix with shift
%oldbmat     :  input allbmatrix

negshiftmat = suggestavg(oldbmat);
bshiftauto = cell(size(oldbmat));

for mrow = 1:size(oldbmat,1);
    for mcol = 1:size(oldbmat,2);
        bshiftauto{mrow, mcol} = oldbmat{mrow, mcol} - negshiftmat(mrow, mcol);
    end
end
