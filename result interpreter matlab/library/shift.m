function new_allbmat = shift(oldbmat)
%new_allbmat = shift(oldbmat)
%shifts events in bmat
%new_allbmat  :  output cell
%oldbmat      :  input cell

fprintf('\nNo_Shift (0), Shift_all(1), Shift_single(2), Shift_suggest(3).\n');
prompt = 'Please enter 0 or 1 or 2 or 3: ';
chshift = input(prompt);
if(chshift ~=0 && chshift ~= 1 && chshift ~= 2 && chshift ~= 3)
    error('Please enter 0 or 1 or 2 or 3 in prompt');
end

switch chshift
    case 0
        new_allbmat = oldbmat;
    case 1
        prompt = 'Shift amount: ';
        shiftnum = input(prompt);
        if(~isnumeric(shiftnum))
            error('input value is not numeric');
        end
        
        new_allbmat = allshift(oldbmat, shiftnum);
    case 2
        prompt = 'Shift amount: ';
        shiftnum = input(prompt);
        if(~isnumeric(shiftnum))
            error('input value is not numeric');
        end
        
        prompt = 'Enter row number of bmatrix on allbmatrix: ';
        srow = input(prompt);
        prompt = 'Enter column number of bmatrix on allbmatrix: ';
        scol = input(prompt);
        
        new_allbmat = shiftone(oldbmat, srow, scol, shiftnum);
    case 3
        
        new_allbmat = shiftavg(oldbmat);
        
    otherwise
        display('error shift amount not 1 or 2 or 3');
    
end

fprintf('\nif you did not set the result to a variable,\n shifted bmatrix is located in "ans"\n');

clear prompt shiftnum chshift oldbmat chmat scol srow