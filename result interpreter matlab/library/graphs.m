function [] = graphs(all_bmat,names)
%graphs(bmatrix)
%graphs number of bmatrix
%bmatrix  :  which matrix with events do you want to plot

fprintf('Size of cell (%d,%d)\n', size(all_bmat,1), size(all_bmat,2));
for i=1:size(all_bmat,1)
    if(isfinite(all_bmat{i,1}))
    	fprintf('{%d,1} A picoscope values of file: %s \n',i,names{i,1});
    end
    if(isfinite(all_bmat{i,2}))
        fprintf('{%d,2} B picoscope values of file: %s \n',i,names{i,1});
    end
end

fprintf('\n');
prompt = 'Enter row number of bmatrix on input all_bmat {_,}: ';
srow = input(prompt);
prompt = 'Enter column number of bmatrix on input all_bmat {,_}: ';
scol = input(prompt);
if(srow > size(all_bmat,1) || scol > size(all_bmat,2) || srow < 1 || scol < 1)
    error('Inputs not within bounds');
end

fprintf('Would you like to... \n (1) Plot all events individually? (w/ A,B comparsion)\n (2) Plot all events in movie? (no A,B comparison, selects A or B)\n (3) Plot all events in stack? (w/ A,B comparison) \n');
prompt = 'Please enter 1, 2, or 3: ';
plottype = input(prompt);
if(plottype ~= 1 && plottype ~= 2 && plottype ~= 3)
    error('Number entered in prompt is not 1,2,3');
end

fprintf('\nSize of Matrix: 1 - %d \nPlease enter the range (start-end) you would like to plot: \n', size(all_bmat{srow,scol},1));
prompt = 'Start:';
startn = input(prompt);
prompt = 'End:';
endn = input(prompt);

switch plottype
    case 1
        ploteventsfig(all_bmat{srow,scol}, startn, endn);
    case 2
        ploteventsmov(all_bmat{srow,scol}, startn, endn);
    case 3
        ploteventsstack(all_bmat{srow,scol}, startn, endn);
    otherwise
        error('Number entered in first prompt is not 1,2,3');
end

