function allbavg = suggestavg(allbmat)
%Generates averages for all matrixes in allbmat
%avgbmat = suggestavg(allbmat)
%avgbmat  :  output matrix with averages as elements
%allbmat  :  input cell with bmatrixes

allbavg = zeros(size(allbmat));

for row = 1:size(allbmat,1)
    for col = 1:size(allbmat,2)
        allbavg(row,col) = mean(mean(allbmat{row,col}(isfinite(allbmat{row,col}))));
    end
end
