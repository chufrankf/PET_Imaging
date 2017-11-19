function numamp = checkamp(dirname)
%checks the amount of amplitudes in the mat file
%numamp   :  number of amplitudes
%dirname  :  name of file.

filenames = what(dirname);
cd(dirname);

ftemp = load(char(filenames.mat(1)));

%fieldnames has 4+ values A has 4 values, A+B has 5 values.
numamp = size(fieldnames(ftemp),1)-3;

cd ..
