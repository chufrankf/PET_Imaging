function [] = graphCoinc(results, allbmatrix)
%Generates Coincidence graphics

for i=1:(size(results{1},1))
    if(results{1}(i) ~= 0)
        countCoin = 0;
        for j=1:(size(results{4},2))
            if(results{4}(i,j) ~= 0)
                
                countCoin = countCoin + 1;
                figure;
                fprintf('\nCoincidence %d - A Amp: %d, B Amp: %d, Time diff: %d, Time of Event: Row{%d},Event(%d)\n', countCoin, results{2}(i,j), results{3}(i,j), results{5}(i,j), i, results{4}(i,j));
                
                titleStr = sprintf('Coincidence %d - A Amp: %d, B Amp: %d, Time diff: %d, Location: Row{%d},Event(%d)', countCoin, results{2}(i,j), results{3}(i,j), results{5}(i,j), i, results{4}(i,j));
                
                hold on
                plot(allbmatrix{i,1}(results{4}(i,j),:));
                title(titleStr);
                xlabel('time');
                ylabel('amplitude (V)');
                
                
                plot(allbmatrix{i,2}(results{4}(i,j),:));
                xlabel('time (ps)');
                ylabel('amplitude');
                hold off
            end
        end
    end
end
