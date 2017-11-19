function [g] = hist111(filetxt,res)
[dat]=tranform111(filetxt);
n = hist3(dat, [res res]);
n1 = n';
n1(size(n,1) + 1, size(n,2) + 1) = 0;
xb = linspace(min(dat(:,1)),max(dat(:,1)),size(n,1)+1);
yb = linspace(min(dat(:,2)),max(dat(:,2)),size(n,1)+1);
h = pcolor(xb,yb,n1);
g=graythresh(h);
end