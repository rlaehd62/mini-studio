package com.github.rlaehd62.entity.file;

import com.github.rlaehd62.entity.auth.Account;
import lombok.*;
import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ID;

    @NonNull
    @OneToOne(targetEntity = Account.class)
    private Account account;

    @NonNull
    @OneToOne(targetEntity = File.class, cascade = CascadeType.REMOVE)
    private File file;
}
