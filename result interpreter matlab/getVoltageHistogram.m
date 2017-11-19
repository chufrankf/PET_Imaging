close all;
clear all;


fprintf('Reminder: You must be in folder with read.m \n');

%startup commands
addpath(pwd);
addpath([pwd,'\library']);

%read all bmatrix from files
dirname = input('Enter directory name: ', 's');
fprintf('\nReading all in directiory and storing into allbmatrix...\n See allbmatrix \n');
fdata = dir(dirname);
cd(dirname);
allbmatrix = cell(size(fdata,1)-2,2); %fdata has 2 unnecessary indexes
names = {''};

for ifor = 3 : size(fdata,1)
    
    
    if(isdir(fdata(ifor).name))
        
        fprintf('\n Data file: %d', ifor-2);
        
        numamp = checkamp(fdata(ifor).name);
        names{ifor-2,1} = fdata(ifor).name;
        
        switch numamp
            case 1
                fprintf(' A \n');
                allbmatrix{ifor-2,1} = bloada(fdata(ifor).name);
            case 2
                fprintf(' A,B \n');
                allbmatrix{ifor-2,1} = bloada(fdata(ifor).name);
                allbmatrix{ifor-2,2} = bloadb(fdata(ifor).name);
            otherwise
               display('There is no A or B in mat files');
        end
    end
    
end

initallbmat = allbmatrix;

fprintf('\n Removing Infinite arrays\n');
allbmatrix = removeInf(allbmatrix);

%clear unneeded variables
clear fdata dirname ifor numamp
fprintf('\ninitallbmat: Raw data (no changes) \nallbmatrix: Matrix After removing infinite values \nnames: Names of Data folders you placed in read Directory');

cd ..

graphVoltHist(allbmatrix,names);

