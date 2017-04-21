insert into users (username, password, enabled)
    values ('admin', '$2a$04$yfl92xWuhU6zhBnIPd3KKOQk8ap5So2Vy5GwV1mPJrAxjfjhW03Xm', true);
insert into authorities (username, authority)
    values ('admin', 'USER');
insert into authorities (username, authority)
    values ('admin', 'ADMIN');

insert into users (username, password, enabled)
    values ('user', '$2a$04$vcp4RK3Aj1V//NcUg3dLY.hJ.BeneOLeUCG3nA89fzuyPLkkXtuli', true);
insert into authorities (username, authority)
    values ('user', 'USER');

