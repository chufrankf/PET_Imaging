function [] = graphRiseHist(inbmat,dataFileNamesmat)

for i=1:size(inbmat,1)
    for j=1:size(inbmat,2)
        
        if(size(inbmat{i,j},2) > 0)
            
            risemat = findrisemat(inbmat{i,j});
            bins = max(risemat) - min(risemat) + 1;
            
            rthist(risemat);
            titlePart = sprintf(' %d bins, (%d,%d)', bins, i, j);
            titleString = strcat(dataFileNamesmat{i}, ' ', titlePart);
            
            title(titleString);
            
        end
        
    end
end
