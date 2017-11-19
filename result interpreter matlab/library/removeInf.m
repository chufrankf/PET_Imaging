function outallbmat = removeInf(inallbmat)
%removes all non finite values (replace with 0)

outallbmat = inallbmat;


for i=1:size(outallbmat,1)
    for j=1:size(outallbmat,2)
        count = 0;
        for k= size(outallbmat{i,j},1):-1:1
            
            if(any(~isfinite(outallbmat{i,j}(k,:))))
                
                outallbmat{i,j}(k,:) = [];
                count = count + 1;
            end
            
        end
        
        fprintf('\n %d infinite arrays removed in {%d,%d} cell', count, i, j);
        
    end
    
end

fprintf('\n');
